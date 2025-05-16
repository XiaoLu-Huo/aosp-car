import com.google.protobuf.gradle.id
import com.google.protobuf.gradle.proto
import org.gradle.internal.declarativedsl.parsing.main

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
    plugins {
        id("javalite") {
            artifact = libs.protoc.gen.javalite.get().toString()
        }
    }
    generateProtoTasks {
        all().forEach {
            it.plugins {
                id("javalite")
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
    api(files("${rootProject.projectDir}/libs/android-car.jar"))
    compileOnly(files("${rootProject.projectDir}/libs/framework-location.jar"))

    api(libs.protobuf.lite)
    api(libs.protobuf.javalite)

    implementation(project(":car-builtin-lib"))
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
