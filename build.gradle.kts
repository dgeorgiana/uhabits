plugins {
    val kotlinVersion = "1.5.0"
    id("com.android.application") version ("7.0.3") apply (false)
    id("org.jetbrains.kotlin.android") version "1.5.30" apply (false)
    id("org.jetbrains.kotlin.kapt") version kotlinVersion apply (false)
    id("org.jetbrains.kotlin.android.extensions") version kotlinVersion apply (false)
    id("org.jetbrains.kotlin.multiplatform") version kotlinVersion apply (false)
    id("org.jlleitschuh.gradle.ktlint") version "10.2.1"
}

apply {
    from("translators.gradle.kts")
}

allprojects {
    repositories {
        google()
        mavenCentral()
        maven(url = "https://plugins.gradle.org/m2/")
        maven(url = "https://oss.sonatype.org/content/repositories/snapshots/")
        maven(url = "https://jitpack.io")
    }
}
