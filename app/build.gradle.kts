plugins {
        alias(libs.plugins.android.application)
        alias(libs.plugins.kotlin.android)
        id("com.google.devtools.ksp")
        id("com.google.gms.google-services")


}

android {
        namespace = "com.example.capstone_pajak"
        compileSdk = 35

        defaultConfig {
                applicationId = "com.example.capstone_pajak"
                minSdk = 24
                targetSdk = 34
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
}
dependencies {

        implementation(libs.androidx.core.ktx)
        implementation(libs.androidx.appcompat)
        implementation(libs.material)
        implementation(libs.androidx.activity)
        implementation(libs.androidx.constraintlayout)
        implementation(libs.tensorflow.lite)
        implementation(libs.tensorflow.lite.support)
        implementation(libs.androidx.datastore.preferences)
        
        // Firebase
        implementation(platform("com.google.firebase:firebase-bom:33.7.0"))
        implementation("com.google.firebase:firebase-auth")
        implementation("com.google.firebase:firebase-analytics")

        // Retrofit untuk komunikasi dengan API
        implementation(libs.retrofit)
        implementation(libs.retrofit.converter.gson)

        // Room Database untuk penyimpanan data lokal
        implementation(libs.androidx.room.runtime)
        implementation(libs.firebase.auth.ktx)
        ksp(libs.androidx.room.compiler) // Migrasi ke KSP

        // Glide untuk memuat gambar profil
        implementation(libs.glide)
        ksp(libs.glide.compiler) // Migrasi ke KSP

        // Unit test dependencies
        testImplementation(libs.junit)

        // UI test dependencies
        androidTestImplementation(libs.androidx.junit)
        androidTestImplementation(libs.androidx.espresso.core)

        // OkHttp untuk API call
        implementation("com.squareup.okhttp3:okhttp:4.11.0")
        // JSON untuk manipulasi data
        implementation("org.json:json:20211205")


        // Navigation Component
        implementation(libs.androidx.navigation.fragment.ktx)
        implementation(libs.androidx.navigation.ui.ktx)


        // Glide
        implementation(libs.glide)
        ksp(libs.glide.compiler)


}
