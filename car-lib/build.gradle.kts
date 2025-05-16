plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "android.car"
    compileSdk = 35

    defaultConfig {
        minSdk = 33
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt")
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    sourceSets {
        getByName("main") {
            java {
                srcDirs("src/main/java")
            }
            aidl.srcDir("src/main/java")
        }
    }
    lint.abortOnError = false

}

afterEvaluate { // 'this' in afterEvaluate is the Project
    tasks.withType<JavaCompile>().configureEach {
        // Inside this block, 'this' is the JavaCompile task instance.
        // So, 'name' refers to 'this.name', 'classpath' to 'this.classpath', etc.

        // 调试信息：打印任务名称，帮助确认配置应用到了正确的任务
        // println("Configuring classpath for task: ${name}")

        // 定义自定义 JAR 文件的路径
        // 请确保这些路径是正确的
        // rootProject is accessible from the Project scope (outer 'afterEvaluate' block's 'this')
        // or via this.project.rootProject if needed, but rootProject should resolve correctly.
        val customFrameworkJar = rootProject.file("libs/framework.jar")
        val customFrameworkWifiJar = rootProject.file("libs/framework-wifi.jar")
        val customCoreLibArtJar = rootProject.file("libs/core-libart.jar")
        val customLocationJar = rootProject.file("libs/framework-location.jar")

        // 创建一个 FileCollection 包含您的自定义 JAR
        // 'this.project' refers to the project to which this JavaCompile task belongs.
        // Using 'this.project.files(...)' is explicit.
        val customLibs = this.project
            .files(
                customFrameworkJar, customFrameworkWifiJar, customCoreLibArtJar, customLocationJar
            )
            .filter { it.exists() }

        if (!customLibs.isEmpty) {
            // 将自定义 JAR 预置到现有编译类路径的前面
            // 'classpath' here refers to 'this.classpath' (the classpath of the JavaCompile task)
            classpath = customLibs + classpath

            // 调试信息：打印修改后的 classpath 的前几个条目
            // println("Updated classpath for ${name} starts with: ${classpath.files.take(3)}")
        } else {
            // 如果 JAR 文件未找到，打印警告
            // 'name' here refers to 'this.name'
            println("Warning: Custom JARs not found at specified paths for task ${name}.")
            println("Searched paths: ${customFrameworkJar.absolutePath}, ${customFrameworkWifiJar.absolutePath},${customCoreLibArtJar.absolutePath}")
        }

        // 确保移除或注释掉旧的 bootstrapClasspath 配置
        // options.bootstrapClasspath = ... // 'options' refers to 'this.options'
    }
}


dependencies {
    compileOnly(files("${rootProject.projectDir}/libs/framework.jar"))
    compileOnly(files("${rootProject.projectDir}/libs/framework-wifi.jar"))
    compileOnly(files("${rootProject.projectDir}/libs/core-libart.jar"))
    api(files("${rootProject.projectDir}/libs/android-car.jar"))
    compileOnly(files("${rootProject.projectDir}/libs/framework-location.jar"))

    implementation(project(":car-builtin-lib"))
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
