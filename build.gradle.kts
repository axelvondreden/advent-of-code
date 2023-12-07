plugins {
    kotlin("jvm") version "1.9.10"
}

group = "com.dude.advent"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.8.10")
}
