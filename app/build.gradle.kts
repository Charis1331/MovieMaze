import Config.Libs

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("android.extensions")
    kotlin("kapt")
}

android {
    compileSdkVersion(Config.SdkVersions.compile)
    defaultConfig {
        applicationId = "com.haris.houlis.moviemaze"
        minSdkVersion(Config.SdkVersions.min)
        targetSdkVersion(Config.SdkVersions.target)
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "com.haris.houlis.moviemaze.TestRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        isCoreLibraryDesugaringEnabled = true

        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
        freeCompilerArgs = freeCompilerArgs + "-Xopt-in=kotlin.RequiresOptIn"
    }

    sourceSets {
        val sharedTestDir = "src/sharedTest/kotlin"
        val test by getting
        test.java.srcDir(sharedTestDir)
        val androidTest by getting
        androidTest.java.srcDir(sharedTestDir)

        map {
            println(it)
            it.java.srcDir("src/${it.name}/kotlin")
        }
    }

    buildFeatures {
        viewBinding = true
    }

    testOptions {
        animationsDisabled = true
        unitTests.isReturnDefaultValues = true
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    with(Libs.Kotlin) {
        implementation(coroutinesCore)
        implementation(coroutinesAndroid)
    }

    coreLibraryDesugaring(Config.Desugaring.desugar)

    //region Jetpack
    with(Libs.Jetpack) {
        implementation(core)
        implementation(appCompat)
        implementation(fragment)
        implementation(rv)
        implementation(constraint)
        implementation(palette)
        implementation(pref)
        implementation(material)
        implementation(paging)
        implementation(swipeToRefresh)
        room.forEach { implementation(it) }
        lifecycle.forEach { implementation(it) }
    }
    //endregion

    with(Libs.Misc) {
        implementation(retrofit)
        implementation(retrofitGson)
        implementation(gson)
        implementation(ratingBar)
        okhttp.forEach { implementation(it) }
        koin.forEach { implementation(it) }
        glide.forEach { implementation(it) }
    }

    //region Testing Libs
    // Unit
    Libs.unitTesting.forEach {
        testImplementation(it)
    }

    // JVM test
    Libs.jvmTesting.forEach {
        testImplementation(it)
    }
    debugImplementation(Libs.fragmentTesting) {
        // https://github.com/android/android-test/issues/731#issuecomment-687201783
        exclude("androidx.test", "monitor")
    }

    // AndroidX Test - Instrumented testing
    Libs.instrumentationTesting.forEach {
        androidTestImplementation(it)
    }
    implementation(Libs.espressoIdlingResource)

    androidTestImplementation(Libs.barista) {
        exclude(group = "org.jetbrains.kotlin")
    }
    //endregion

    // Annotation Processors
    Libs.kapts.forEach {
        kapt(it)
    }
}
