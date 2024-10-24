import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.Properties

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.aboutlibrariesPlugin)
}

android {
    namespace = "com.cyb3rko.pazzword"
    compileSdk = 35
    defaultConfig {
        applicationId = "com.cyb3rko.pazzword"
        minSdk = 21
        targetSdk = 35
        versionCode = 12
        versionName = "1.2.4"
        resourceConfigurations.addAll(arrayOf("en", "de"))
        signingConfig = signingConfigs.getByName("debug")
    }
    signingConfigs {
        create("signingConf") {
            val properties = Properties()
            properties.load(project.rootProject.file("local.properties").inputStream())

            storeFile = file(properties.getProperty("signing.file"))
            storePassword = properties.getProperty("signing.password")
            keyAlias = properties.getProperty("signing.key.alias")
            keyPassword = properties.getProperty("signing.key.password")
        }
    }
    buildTypes {
        release {
            isMinifyEnabled = true
            isCrunchPngs = true
            isShrinkResources = true
            signingConfig = signingConfigs.getByName("signingConf")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlin {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }
    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
}

aboutLibraries {
    excludeFields = arrayOf("generated")
}

if (project.hasProperty("sign")) {
    android {
        signingConfigs {
            create("release") {
                storeFile = file(System.getenv("KEYSTORE_FILE"))
                storePassword = System.getenv("KEYSTORE_PASSWD")
                keyAlias = System.getenv("KEYSTORE_KEY_ALIAS")
                keyPassword = System.getenv("KEYSTORE_KEY_PASSWD")
            }
        }
    }
    android.buildTypes.getByName("release").signingConfig = android.signingConfigs.getByName("release")
}

dependencies {
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.airbnb.lottie)
    implementation(libs.cyb3rko.about.icons)
    implementation(libs.google.android.material)
    implementation(libs.gosimple.nbvcxz)
    implementation(libs.medyo.android.about.page)
    implementation(libs.mikepenz.aboutlibraries)
    implementation(libs.mikepenz.aboutlibraries.core)
}
