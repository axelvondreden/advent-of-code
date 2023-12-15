import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("jvm")
    id("org.jetbrains.compose")
}

group = "com.dude.advent"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    google()
}

dependencies {
    implementation(compose.desktop.currentOs)
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0-RC")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.9.21")
    implementation("com.github.ajalt.mordant:mordant:2.2.0")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.8.10")
}

compose.desktop {
    application {
        mainClass = "RunnerKt"

        nativeDistributions {
            targetFormats(TargetFormat.Msi)
            packageName = "advent-of-code"
            packageVersion = "1.0.0"
        }
    }
}