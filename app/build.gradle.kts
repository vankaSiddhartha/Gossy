plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.bhaskardamayanthi.gossy"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.bhaskardamayanthi.gossy"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
    }
    buildToolsVersion = "34.0.0"
}

dependencies {

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("com.github.bumptech.glide:glide:4.16.0")
    implementation("com.github.f0ris.sweetalert:library:1.6.2")
    implementation("com.google.android.gms:play-services-auth-api-phone:18.0.1")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("de.hdodenhof:circleimageview:3.1.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.google.firebase:firebase-auth:22.3.0")
    implementation("pl.droidsonroids.gif:android-gif-drawable:1.2.23")
    implementation("com.google.firebase:firebase-messaging:23.4.0")
    implementation("com.google.firebase:firebase-database:20.3.0")
    implementation("com.google.firebase:firebase-storage:20.3.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.5")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.5")

    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.6.2")
    implementation("com.squareup.retrofit2:converter-gson:2.6.0")
    //com.facebook.shimmer:shimmer:0.5.0
    implementation("com.facebook.shimmer:shimmer:0.5.0")
    implementation("dev.shreyaspatil.EasyUpiPayment:EasyUpiPayment:3.0.3")
    implementation ("com.google.zxing:core:3.4.1")
    implementation ("com.journeyapps:zxing-android-embedded:4.2.0")

}