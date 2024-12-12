pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
        maven { url = uri("https://jitpack.io") }
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://www.jitpack.io") }
    }

    versionCatalogs {
        create("libs") {
            // Versi
            version("kotlin", "1.9.22")
            version("agp", "8.6.1")
            version("coreKtx", "1.15.0")
            version("appcompat", "1.7.0")
            version("material", "1.12.0")
            version("constraintlayout", "2.2.0")
            version("navigationFragmentKtx", "2.8.4")
            version("navigationUiKtx", "2.8.4")
            version("retrofit", "2.9.0")
            version("room", "2.5.0")
            version("glide", "4.15.1")
            version("tensorflowLite", "2.12.0")
            version("tensorflowLiteSupport", "0.4.3")

            // Library
            library("androidx-core-ktx", "androidx.core", "core-ktx").versionRef("coreKtx")
            library("androidx-appcompat", "androidx.appcompat", "appcompat").versionRef("appcompat")
            library("material", "com.google.android.material", "material").versionRef("material")
            library("androidx-constraintlayout", "androidx.constraintlayout", "constraintlayout").versionRef("constraintlayout")
            library("androidx-navigation-fragment-ktx", "androidx.navigation", "navigation-fragment-ktx").versionRef("navigationFragmentKtx")
            library("androidx-navigation-ui-ktx", "androidx.navigation", "navigation-ui-ktx").versionRef("navigationUiKtx")
            library("retrofit", "com.squareup.retrofit2", "retrofit").versionRef("retrofit")
            library("retrofit-converter-gson", "com.squareup.retrofit2", "converter-gson").versionRef("retrofit")
            library("androidx-room-runtime", "androidx.room", "room-runtime").versionRef("room")
            library("androidx-room-compiler", "androidx.room", "room-compiler").versionRef("room")
            library("glide", "com.github.bumptech.glide", "glide").versionRef("glide")
            library("tensorflow-lite", "org.tensorflow", "tensorflow-lite").versionRef("tensorflowLite")
            library("tensorflow-lite-support", "org.tensorflow", "tensorflow-lite-support").versionRef("tensorflowLiteSupport")
        }
    }
}

rootProject.name = "capstone_pajak"
include(":app")
