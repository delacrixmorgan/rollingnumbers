import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.kmp.library)

    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.maven.publish)
}

mavenPublishing {
    coordinates(
        groupId = "com.dontsaybojio",
        artifactId = "rollingnumbers",
        version = libs.versions.rollingnumbers.get()
    )
    pom {
        name.set("Rolling Numbers")
        description.set("Odometer Scrolling Effect TextView \uD83C\uDFB0")
        inceptionYear.set("2025")
        url.set("https://github.com/delacrixmorgan/rollingnumbers")
        licenses {
            license {
                name.set("GNU General Public License v3.0")
                url.set("https://github.com/delacrixmorgan/rollingnumbers/blob/main/LICENSE.md")
            }
        }
        developers {
            developer {
                id.set("delacrixmorgan")
                name.set("Morgan Koh")
                email.set("delacrixmorgan@gmail.com")
            }
        }
        scm {
            url.set("https://github.com/delacrixmorgan/rollingnumbers")
        }
    }
    publishToMavenCentral(automaticRelease = true)
    signAllPublications()
}

kotlin {
    androidLibrary {
        namespace = "com.dontsaybojio.rollingnumbers.lib"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
    iosX64()
    iosArm64()
    iosSimulatorArm64()
    jvm("desktop")
    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser()
    }
    sourceSets {
        commonMain.dependencies {
            implementation(libs.compose.material3)
            implementation(libs.compose.material.icons.extended)
        }
    }
}