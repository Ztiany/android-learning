plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.app.byteweave.plugin.all")
}

android {
    namespace = "me.ztiany.apm"
    compileSdk = 34

    defaultConfig {
        applicationId = "me.ztiany.apm"
        minSdk = 21
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }

    externalNativeBuild {
        cmake {
            path = file("src/main/cpp/CMakeLists.txt")
            version = "3.22.1"
        }
    }

    buildFeatures {
        viewBinding = true
        prefab = true
    }

    packagingOptions {
        pickFirst("**/libbytehook.so")
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    implementation("com.jakewharton.timber:timber:4.7.1")

    implementation("com.bytedance:bytehook:1.0.10")
    implementation("com.squareup.leakcanary:leakcanary-android:2.14")
    // https://github.com/bytedance/memory-leak-detector
    //implementation("com.github.bytedance:memory-leak-detector:0.2.1")
}

extensions.configure(com.app.byteweave.plugin.ByteweaveConfiguration::class) {
    legalImage {
        enabled = true
        rules {
            "android.widget.ImageView" replaceWith "me.ztiany.apm.aspect.bitmap.MonitorImageView"
            "androidx.appcompat.widget.AppCompatImageView" replaceWith "me.ztiany.apm.aspect.bitmap.MonitorAppCompatImageView"
        }
    }

    viewThrottle {
        enabled = true
        include("me.ztiany.apm")
        checker("me.ztiany.apm.aspect.throttle.ClickThrottler", "check")
        includeAnnotation("me.ztiany.apm.aspect.throttle.ThrottleClick")
        addViewOnClickListenerHookPoint()
    }
}