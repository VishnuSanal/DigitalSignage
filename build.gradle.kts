import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("jvm")
    id("org.jetbrains.compose")
}

group = "in.ac.gecskp"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    google()
}

dependencies {
    // Note, if you develop a library, you should use compose.desktop.common.
    // compose.desktop.currentOs should be used in launcher-sourceSet
    // (in a separate module for demo project and in testMain).
    // With compose.desktop.common you will also lose @Preview functionality
    implementation(compose.desktop.currentOs)

    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-scalars:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")
    implementation("com.russhwolf:multiplatform-settings-no-arg:1.1.1")
}

compose.desktop {
    application {
        mainClass = "MainKt"

        //      https://github.com/JetBrains/compose-multiplatform/blob/master/tutorials/Native_distributions_and_local_execution/README.md
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            includeAllModules = true

            packageName = "DigitalSignage"
            packageVersion = "1.0.0"

            description = "GEC PKD Digital Signage System (Client)"
            copyright = "Copyright (C) 2024 Vishnu Sanal T"

            licenseFile.set(project.file("LICENSE"))
//            macOS {
//                iconFile.set(project.file("icon.png"))
//            }
//            windows {
//                iconFile.set(project.file("icon.png"))
//            }
//            linux {
//                iconFile.set(project.file("icon.png"))
//            }
        }

        buildTypes.release.proguard {
            obfuscate.set(true)
            optimize.set(true)
        }
    }
}
