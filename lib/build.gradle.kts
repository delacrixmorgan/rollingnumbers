plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.kmp.library)

    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.maven.publish)
}

mavenPublishing {
    coordinates(
        groupId = "io.github.delacrixmorgan",
        artifactId = "rollingnumbers",
        version = "0.2.1"
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
    sourceSets {
        commonMain.dependencies {
            implementation(compose.material3)
            implementation(compose.materialIconsExtended)
            implementation(compose.components.uiToolingPreview)
        }
    }
}