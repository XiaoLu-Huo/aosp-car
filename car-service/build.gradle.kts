import com.google.protobuf.gradle.proto

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.protobuf)
}

android {
    namespace = "com.android.car"
    compileSdk = 35

    defaultConfig {
        minSdk = 33

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
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
    sourceSets["main"].proto { srcDir("src/main/proto") }
    sourceSets["main"].aidl.srcDir("src/main/java")
    buildFeatures {
        aidl = true
    }
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
        val customLocationJar = rootProject.file("libs/framework-location.jar")
        val customBluetoothJar = rootProject.file("libs/framework-bluetooth.jar")
        val customConnectivityJar = rootProject.file("libs/framework-connectivity.jar")
        val customConnectivityTJar = rootProject.file("libs/framework-connectivity-t.jar")
        val customLibs = this.project
            .files(
                customFrameworkJar,
                customFrameworkWifiJar,
                customLocationJar,
                customBluetoothJar,
                customConnectivityJar,
                customConnectivityTJar
            )
            .filter { it.exists() }
        if (!customLibs.isEmpty) {
            classpath = customLibs + classpath
        } else {
            println("Warning: Custom JARs not found at specified paths for task ${name}.")
            println("Searched paths: ${customFrameworkJar.absolutePath}, ${customFrameworkWifiJar.absolutePath}")
        }
    }
}

dependencies {
    compileOnly(files("${rootProject.projectDir}/libs/framework.jar"))
    compileOnly(files("${rootProject.projectDir}/libs/framework-wifi.jar"))
    compileOnly(files("${rootProject.projectDir}/libs/core-libart.jar"))
    compileOnly(files("${rootProject.projectDir}/libs/android-car.jar"))
    compileOnly(files("${rootProject.projectDir}/libs/framework-location.jar"))
    compileOnly(files("${rootProject.projectDir}/libs/framework-bluetooth.jar"))

    compileOnly(files("${rootProject.projectDir}/libs/framework-connectivity.jar"))
    compileOnly(files("${rootProject.projectDir}/libs/framework-connectivity-t.jar"))
    compileOnly(files("${rootProject.projectDir}/libs/framework-statsd.jar"))
    compileOnly(files("${rootProject.projectDir}/libs/framework-tethering.jar"))

    api(libs.protobuf.javalite)

    implementation(project(":car-lib"))
    implementation(project(":car-builtin-lib"))
    implementation(project(":hardware-vehicle"))
    implementation(project(":procfs-inspector"))
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}

afterEvaluate {
    tasks.withType<JavaCompile>().configureEach {
        listOf("debug", "release").forEach { buildType ->
            val capitalized = buildType.replaceFirstChar { it.uppercase() }
            dependsOn("modify${capitalized}ProtoFiles")
        }
    }
}

// 定义自定义任务来执行 Python 脚本
listOf("debug", "release").forEach{ buildType ->

    val capitalized = buildType.replaceFirstChar { it.uppercase() }

    tasks.register<Exec>("modify${capitalized}ProtoFiles") {
        dependsOn("generate${capitalized}Proto") // 明确依赖 Proto 生成任务
        commandLine("python3",
            "${project.rootDir}/scripts/process_java_file_for_proto.py",
            "build/generated/source/proto/${buildType}/java",
            "--exclude", "com/android/car/telemetry/AtomsProto.java"  // 需要排除的特定文件
        )
    }
}
