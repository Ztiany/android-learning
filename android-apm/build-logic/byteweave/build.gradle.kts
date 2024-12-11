import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `kotlin-dsl`
}

group = "com.android.app.build"

// Configure the build-logic plugins to target JDK 17
// This matches the JDK used to build the project, and is not related to what is running on device.
java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}
tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
}

dependencies {
    //gradleApi()

    implementation("com.android.tools.build:gradle:8.2.1")

    compileOnly("commons-io:commons-io:2.6")
    compileOnly("commons-codec:commons-codec:1.15")

    compileOnly("org.ow2.asm:asm-commons:9.2")
    compileOnly("org.ow2.asm:asm-tree:9.2")
    implementation("org.ow2.asm:asm-util:9.2")

    implementation("com.google.code.gson:gson:2.11.0")
}

tasks {
    validatePlugins {
        enableStricterValidation = true
        failOnWarning = true
    }
}

gradlePlugin {
    plugins {
        // modularization plugins
        register("ByteweavePlugin") {
            id = "com.app.byteweave.plugin.all"
            version = "1.0.0"
            implementationClass = "com.app.byteweave.plugin.ByteweavePlugin"
        }
    }
}
