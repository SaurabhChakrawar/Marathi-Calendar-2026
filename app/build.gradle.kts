import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
    id("org.jetbrains.kotlin.plugin.serialization")
}

// Load keystore credentials from keystore.properties (not committed to git)
val keystorePropertiesFile = rootProject.file("keystore.properties")

android {
    namespace = "com.saurabh.marathicalendar"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.saurabh.marathicalendar"
        minSdk = 24
        targetSdk = 35
        versionCode = 9
        versionName = "1.1.4"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables { useSupportLibrary = true }
    }

    signingConfigs {
        create("release") {
            if (keystorePropertiesFile.exists()) {
                val props = Properties()
                props.load(keystorePropertiesFile.inputStream())
                storeFile = file(props.getProperty("storeFile"))
                storePassword = props.getProperty("storePassword")
                keyAlias = props.getProperty("keyAlias")
                keyPassword = props.getProperty("keyPassword")
            }
        }
    }

    buildTypes {
        release {
            if (keystorePropertiesFile.exists()) {
                signingConfig = signingConfigs.getByName("release")
            }
            isMinifyEnabled = true
            isShrinkResources = true
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

    kotlinOptions { jvmTarget = "17" }

    buildFeatures { compose = true }

    packaging {
        resources { excludes += "/META-INF/{AL2.0,LGPL2.1}" }
    }
}

// Fix: Gradle incremental builds leave stale " 2.xml" files in packaged_res
// that cause AAPT2 "Failed file name validation" errors. Delete them before
// each resource packaging task runs so only fresh files are processed.
afterEvaluate {
    listOf("packageDebugResources", "packageReleaseResources").forEach { taskName ->
        tasks.findByName(taskName)?.doFirst {
            val packedResDir = layout.buildDirectory.dir("intermediates/packaged_res").get().asFile
            if (packedResDir.exists()) {
                packedResDir.walkTopDown().filter { file ->
                    file.isFile && file.name.contains(" ")
                }.forEach { badFile ->
                    println("Removing stale file: ${badFile.name}")
                    badFile.delete()
                }
            }
        }
    }
}

dependencies {
    val composeBom = platform("androidx.compose:compose-bom:2025.01.01")
    implementation(composeBom)

    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material:material-icons-extended")
    implementation("androidx.compose.foundation:foundation")

    implementation("androidx.navigation:navigation-compose:2.8.5")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.7")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.8.7")
    implementation("androidx.activity:activity-compose:1.9.3")
    implementation("androidx.core:core-ktx:1.15.0")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")

    // Google AdMob
    implementation("com.google.android.gms:play-services-ads:23.6.0")

    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}
