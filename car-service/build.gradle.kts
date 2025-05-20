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
        minSdk = 24

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
        val customLibs = this.project
            .files(
                customFrameworkJar, customFrameworkWifiJar, customLocationJar
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

    compileOnly(files("${rootProject.projectDir}/libs/framework-connectivity.jar"))
    compileOnly(files("${rootProject.projectDir}/libs/framework-connectivity-t.jar"))
    compileOnly(files("${rootProject.projectDir}/libs/framework-statsd.jar"))
    compileOnly(files("${rootProject.projectDir}/libs/framework-tethering.jar"))

//    api(libs.protobuf.lite)
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