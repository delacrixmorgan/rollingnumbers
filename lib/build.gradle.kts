plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.library)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.compose.compiler)
    id("maven-publish")
}

kotlin {
    androidTarget()
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { target ->
        target.binaries.framework {
            baseName = "RollingNumbers"
            isStatic = true
        }
    }
    jvm("desktop")
    sourceSets {
        commonMain.dependencies {
            implementation(project.dependencies.platform(libs.compose.bom))
            implementation(compose.material3)
            implementation(compose.materialIconsExtended)
            implementation(compose.components.uiToolingPreview)
        }
    }
}

android {
    namespace = "io.dontsayboj.rollingnumbers"
    compileSdk = 36

    defaultConfig {
        minSdk = 24
        version = "0.0.1"
        consumerProguardFiles("consumer-rules.pro")
    }

    publishing {
        singleVariant("release")
    }
}