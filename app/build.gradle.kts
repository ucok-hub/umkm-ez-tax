plugins {
        alias(libs.plugins.android.application)
        alias(libs.plugins.kotlin.android)
        id("com.google.devtools.ksp")
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
        aaptOptions {
                noCompress("tflite")
        }

        // Adding folder `assets` for `.tflite` files
        sourceSets {
                getByName("main") {
                        assets.srcDirs("src/main/assets")
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
        // Core libraries
        implementation(libs.androidx.core.ktx)
        implementation(libs.androidx.appcompat)
        implementation(libs.material)
        implementation(libs.androidx.activity)
        implementation(libs.androidx.constraintlayout)

        // TensorFlow Lite Core and Task Libraries
        implementation(libs.tensorflow.lite)
        implementation(libs.tensorflow.lite.support)
        implementation ("org.tensorflow:tensorflow-lite:2.10.0") // Use your compatible version
        implementation ("org.tensorflow:tensorflow-lite-support:0.3.1")

        // DataStore Preferences Library
        implementation("androidx.datastore:datastore-preferences:1.0.0")

        // Room Database
        implementation(libs.androidx.room.runtime)
        ksp(libs.androidx.room.compiler)

        // Glide
        implementation(libs.glide)
        ksp(libs.glide.compiler)

        // Navigation Component
        implementation(libs.androidx.navigation.fragment.ktx)
        implementation(libs.androidx.navigation.ui.ktx)

        // Unit Test
        testImplementation(libs.junit)

        // UI Test
        androidTestImplementation(libs.androidx.junit)
        androidTestImplementation(libs.androidx.espresso.core)

        implementation("org.json:json:20210307")

}
