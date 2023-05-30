plugins {
    id("com.android.application")
    kotlin("android")
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.jojo.compose_notes_app"
    compileSdk = ProjectConfig.compileSdk

    defaultConfig {
        applicationId = ProjectConfig.appId
        minSdk = ProjectConfig.minSdk
        targetSdk = ProjectConfig.targetSdk
        versionCode = ProjectConfig.versionCode
        versionName = ProjectConfig.versionName

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled= false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
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
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = Compose.composeCompilerVersion
    }
    packagingOptions {
        resources.excludes.addAll(listOf("/META-INF/AL2.0", "/META-INF/LGPL2.1"))
    }
    kapt {
        correctErrorTypes = true
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.8.0")
    implementation(platform("org.jetbrains.kotlin:kotlin-bom:1.8.0"))
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.3.1")

    // Compose
    val composeBom = platform(Compose.bom)
    implementation(composeBom)
    implementation(Compose.m3)
    implementation(Compose.ui)
    implementation(Compose.uiGraphics)
    implementation(Compose.icons)
    implementation(Compose.sizeClass)
    implementation(Compose.activity)
    implementation(Compose.viewModel)
    implementation(Compose.navigation)
    implementation(Testing.composePreview)

    // Room
    implementation(Room.runtime)
    implementation(Room.ktx)
    kapt(Room.compiler)

    // Hilt
    implementation(Hilt.main)
    kapt(Hilt.compiler)
    implementation(Hilt.navigationCompose)

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    androidTestImplementation(composeBom)
    debugImplementation(Testing.composeTooling)
    androidTestImplementation(Testing.composeJunit)
    debugImplementation(Testing.composeManifest)
}