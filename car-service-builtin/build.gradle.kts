plugins {
    id("java-library")
    alias(libs.plugins.jetbrains.kotlin.jvm)
}
java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}
kotlin {
    compilerOptions {
        jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17
    }
}

afterEvaluate {
    tasks.withType<JavaCompile>().configureEach {
        val customFrameworkJar = rootProject.file("libs/framework.jar")
        val customLibs = this.project
            .files(
                customFrameworkJar
            )
            .filter { it.exists() }
        if (!customLibs.isEmpty) {
            classpath = customLibs + classpath
        } else {
            println("Warning: Custom JARs not found at specified paths for task ${name}.")
            println("Searched paths: ${customFrameworkJar.absolutePath}")
        }
    }
}


dependencies {
    compileOnly(files("${rootProject.projectDir}/libs/framework.jar"))

    implementation(project(":car-lib"))
    implementation(project(":car-builtin-lib"))
    implementation(project(":car-service"))
}
