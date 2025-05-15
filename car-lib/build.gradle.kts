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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    sourceSets {
        getByName("main") {
            java {
                srcDirs("src/main/java")
            }
            aidl.srcDir("src/main/java")
        }
    }
}

gradle.projectsEvaluated {
    tasks.withType<JavaCompile> {
        val fileSet = options.bootstrapClasspath?.files ?: emptySet<File>()
        val fileList = mutableListOf<File>()
        fileList.add(File("${rootProject.projectDir}/libs/framework.jar"))
        fileList.add(File("${rootProject.projectDir}/libs/framework-wifi.jar"))
        fileList.add(File("${rootProject.projectDir}/libs/core-libart.jar"))
        fileList.addAll(fileSet)
        options.bootstrapClasspath = files(fileList)
    }
}


dependencies {
    compileOnly(files("${rootProject.projectDir}/libs/framework.jar"))
    compileOnly(files("${rootProject.projectDir}/libs/framework-wifi.jar"))
    compileOnly(files("${rootProject.projectDir}/libs/core-libart.jar"))

    implementation(project(":car-builtin-lib"))
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
