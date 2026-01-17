// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.2.0" apply false
    kotlin("android") version "1.9.20" apply false
    kotlin("kapt") version "1.9.20" apply false
    id("com.google.gms.google-services") version "4.4.0" apply false
    id("com.google.firebase.crashlytics") version "2.9.10" apply false
    id("com.google.dagger.hilt.android") version "2.48" apply false
    id("org.jlleitschuh.gradle.ktlint") version "12.1.1" apply false
    id("io.gitlab.arturbosch.detekt") version "1.23.6" apply false
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
