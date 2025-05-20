import com.google.protobuf.gradle.proto

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.protobuf)
}

android {
    namespace = "android.automotive"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }

    sourceSets["main"].proto {
        srcDir("src/main/java")
    }
    sourceSets["main"].java {
        srcDirs("src/main/java")
    }

    sourceSets["main"].aidl.srcDir("src/main/java")
    buildFeatures {
        aidl = true
    }
}

afterEvaluate {
    tasks.withType<JavaCompile>().configureEach {
        val customFrameworkJar = rootProject.file("libs/framework.jar")
        val customFrameworkWifiJar = rootProject.file("libs/framework-wifi.jar")
        val customFrameworkStatsdJar = rootProject.file("libs/framework-statsd.jar")
        val customLibs = this.project
            .files(
                customFrameworkJar, customFrameworkWifiJar, customFrameworkStatsdJar
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

dependencies {
    api(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    compileOnly(files("${rootProject.projectDir}/libs/framework-statsd.jar"))
    implementation(project(":car-builtin-lib"))


    api(libs.protobuf.javalite)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}