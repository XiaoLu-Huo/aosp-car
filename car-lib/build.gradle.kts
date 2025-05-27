import com.google.protobuf.gradle.proto

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.protobuf)
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

    sourceSets["main"].proto { srcDir("src/main/java") }
    sourceSets["main"].aidl.srcDir("src/main/java")
    lint.abortOnError = false
}



protobuf {
    protoc {
        artifact = libs.protoc.protoc.get().toString()
    }
    generateProtoTasks {
        all().forEach { task ->
            // For Android, Javalite is recommended for smaller code size.
            task.builtins {
                create("java") { // Use 'create' for safer configuration
                    option("lite")
                }
            }
        }
    }
}


afterEvaluate {
    tasks.withType<JavaCompile>().configureEach {
        val customFrameworkJar = rootProject.file("libs/framework.jar")
        val customFrameworkWifiJar = rootProject.file("libs/framework-wifi.jar")
        val customCoreLibArtJar = rootProject.file("libs/core-libart.jar")
        val customLocationJar = rootProject.file("libs/framework-location.jar")
        val customLibs = this.project
            .files(
                customFrameworkJar, customFrameworkWifiJar, customCoreLibArtJar, customLocationJar
            )
            .filter { it.exists() }
        if (!customLibs.isEmpty) {
            classpath = customLibs + classpath
        } else {
            println("Warning: Custom JARs not found at specified paths for task ${name}.")
            println("Searched paths: ${customFrameworkJar.absolutePath}, ${customFrameworkWifiJar.absolutePath},${customCoreLibArtJar.absolutePath}")
        }
    }
}


dependencies {
    compileOnly(files("${rootProject.projectDir}/libs/framework.jar"))
    compileOnly(files("${rootProject.projectDir}/libs/framework-wifi.jar"))
    compileOnly(files("${rootProject.projectDir}/libs/core-libart.jar"))
    compileOnly(files("${rootProject.projectDir}/libs/android-car.jar"))
    compileOnly(files("${rootProject.projectDir}/libs/framework-location.jar"))

//    api(libs.protobuf.lite)
    api(libs.protobuf.javalite)

    implementation(project(":car-builtin-lib"))
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}

// 此脚本通过解压 AAR 并提取 classes.jar，模拟了 AOSP 生成 android.car.jar 的过程
// 适用场景：当项目因依赖 AIDL 或 Android 资源而必须使用 Android 库模块，但最终需要纯 JAR 文件时。
tasks.create<Copy>("unzipAar") {
    description = "Assembles android.car.jar"
    group = "Build"
    mustRunAfter("build")

    from(zipTree("${project.projectDir}/build/outputs/aar/car-lib-debug.aar"))
    include("classes.jar")
    into("${project.projectDir}/build/libs")
    rename("classes.jar", "android.car.jar")

    doLast {
        println("Assembles android.car.jar")
    }
}
// 在 build.gradle.kts 中添加依赖，使 unzipAar 自动在构建后执行：
tasks.getByName("build").finalizedBy("unzipAar")

