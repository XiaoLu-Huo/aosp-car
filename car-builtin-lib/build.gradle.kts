plugins {
    id("java-library")
    alias(libs.plugins.jetbrains.kotlin.jvm)
}
java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}
kotlin {
    compilerOptions {
        jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11
    }
}

dependencies {
    compileOnly(files("${rootProject.projectDir}/libs/framework.jar"))
    compileOnly(files("${rootProject.projectDir}/libs/framework-bluetooth.jar"))
    compileOnly(files("${rootProject.projectDir}/libs/core-libart.jar"))

}

tasks.getByName("compileJava").dependsOn("generateEventLogTags")

/**
 * Generate EventLogTags when build
 * Usage: java-event-log-tags.py [-o output_file] <input_file> <merged_tags_file>
 */
tasks.create<Exec>("generateEventLogTags") {
    description = "Generate EventLogTags java class by python3."
    group = "Build"

    executable = "python3"
    args(
        "${rootProject.projectDir}/scripts/java-event-log-tags.py",
        "-o",
        "${project.projectDir}/src/main/java/android/car/builtin/util/EventLogTags.java",
        "${project.projectDir}/src/main/java/android/car/builtin/util/EventLogTags.logtags"
    )

    doLast {
        println("Generated EventLogTags successfully")
    }
}


