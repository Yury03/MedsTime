plugins {
    alias(libs.plugins.android.libriary)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.google.devtools.ksp)
}

android {
    namespace = "com.example.data"
    compileSdk = 34

    defaultConfig {
        minSdk = 26
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        getByName("debug") {
            isMinifyEnabled = true
            proguardFiles("proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
//    kotlinOptions {
//        jvmTarget = "19"
//    }
}



dependencies {
    implementation(libs.kotlin.stdlib)
    implementation(libs.core.ktx)
    //modules
    implementation(project(":domain"))

    //databases
    implementation(libs.bundles.room)
    ksp(libs.androidx.room.compiler)

    //etc
    implementation(libs.gson)

    //tests
    testImplementation(libs.bundles.moduleTests)
    androidTestImplementation(libs.bundles.instrumentalTests)
}
