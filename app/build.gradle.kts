plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("kotlin-android")
    id("kotlin-parcelize")
    id("androidx.navigation.safeargs.kotlin")
}

android {
    namespace = "com.example.medstime"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.medstime"
        minSdk = 27
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("debug")
        }
        getByName("debug") {
            isMinifyEnabled = false
            proguardFiles("proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {
        viewBinding = true
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.4"
    }

}


dependencies {
    implementation(libs.kotlin.stdlib)
    implementation(libs.core.ktx)

    //modules
    implementation(project(":data"))
    implementation(project(":domain"))


    //data
    implementation(libs.gson)

    //dependency injection
    implementation(libs.bundles.diKoin)

    //user interface
    implementation(libs.appcompat)
//    implementation(libs.bundles.camera)
    implementation(libs.constraintlayout)
    implementation(libs.navigation.fragment.ktx)
    implementation(libs.navigation.ui.ktx)
    implementation(libs.expandablelayout)
    implementation(libs.material)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.lifecycle.livedata.ktx)
    implementation(libs.swipe)

    //---
    implementation(libs.kotlinx.serialization.json)

    //Compose
    implementation(libs.androidx.foundation.layout.android)
    implementation(libs.androidx.material3)
    implementation(libs.bundles.compose)
    implementation(libs.androidx.ui.tooling.preview.android)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.lifecycle)
    implementation(libs.androidx.runtime)
    implementation(libs.androidx.runtime.livedata)
    implementation(libs.androidx.runtime.rxjava2)
    implementation(libs.androidx.navigation.compose)

    //debug
    debugImplementation(libs.bundles.debug)

    //tests
    testImplementation(libs.bundles.moduleTests)
    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.bundles.instrumentalTests)
}