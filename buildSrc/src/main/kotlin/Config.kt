import Config.Libs.Vers.appCompatVers
import Config.Libs.Vers.archCoreTesting
import Config.Libs.Vers.baristaVers
import Config.Libs.Vers.constraintVers
import Config.Libs.Vers.coreVers
import Config.Libs.Vers.coroutinesTestVers
import Config.Libs.Vers.coroutinesVersion
import Config.Libs.Vers.espressoCoreVers
import Config.Libs.Vers.fragmentTestingVers
import Config.Libs.Vers.fragmentVers
import Config.Libs.Vers.glideVers
import Config.Libs.Vers.gsonVers
import Config.Libs.Vers.junitExtVers
import Config.Libs.Vers.junitVers
import Config.Libs.Vers.koinVers
import Config.Libs.Vers.lifecycleVers
import Config.Libs.Vers.materialVers
import Config.Libs.Vers.mockServerVers
import Config.Libs.Vers.mockitoKotlinVers
import Config.Libs.Vers.mockitoVers
import Config.Libs.Vers.okhttpVers
import Config.Libs.Vers.pagingVers
import Config.Libs.Vers.paletteVers
import Config.Libs.Vers.preferenceVers
import Config.Libs.Vers.ratingBarVers
import Config.Libs.Vers.retrofitVers
import Config.Libs.Vers.roboVers
import Config.Libs.Vers.roomVers
import Config.Libs.Vers.rvVers
import Config.Libs.Vers.savedStateVers
import Config.Libs.Vers.swipeRefreshVers
import Config.Libs.Vers.testCoreVers
import Config.Libs.Vers.truthVers
import org.gradle.api.artifacts.dsl.RepositoryHandler

fun RepositoryHandler.deps() {
    google().content {
        includeGroupByRegex("com\\.android\\..*")
        includeGroupByRegex("com\\.google\\..*")
        includeGroupByRegex("androidx\\..*")

        includeGroup("com.android")
        includeGroup("android.arch.lifecycle")
        includeGroup("android.arch.core")
    }
    mavenCentral()
    jcenter()
}

object Config {

    private const val agpVersion = "4.1.1"
    private const val kotlinVersion = "1.3.72"
    private const val desugarVersion = "1.1.1"

    object SdkVersions {
        const val compile = 29
        const val target = 29
        const val min = 26
    }

    object Plugins {
        const val android = "com.android.tools.build:gradle:$agpVersion"
        const val kotlin = "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion"
    }

    object Desugaring {
        const val desugar = "com.android.tools:desugar_jdk_libs:$desugarVersion"
    }

    object Libs {

        object Vers {
            const val coroutinesVersion = "1.3.9"

            const val coreVers = "1.5.0-alpha05"
            const val appCompatVers = "1.3.0-alpha01"
            const val fragmentVers = "1.3.0-alpha06"
            const val rvVers = "1.2.0-alpha04"
            const val constraintVers = "2.0.0-beta7"
            const val paletteVers = "1.0.0"
            const val preferenceVers = "1.1.1"
            const val materialVers = "1.3.0-alpha01"
            const val pagingVers = "3.0.0-alpha04"
            const val roomVers = "2.3.0-alpha04"
            const val lifecycleVers = "2.3.0-alpha05"
            const val savedStateVers = "1.1.0-beta01"
            const val swipeRefreshVers = "1.1.0"

            const val retrofitVers = "2.9.0"
            const val glideVers = "4.11.0"
            const val koinVers = "2.2.1"
            const val gsonVers = "2.8.6"
            const val okhttpVers = "4.8.1"
            const val ratingBarVers = "1.4.0"

            const val junitVers = "4.12"
            const val junitExtVers = "1.1.2"
            const val mockitoVers = "1.10.19"
            const val testCoreVers = "1.3.0"
            const val roboVers = "4.4"
            const val truthVers = "1.0.1"
            const val fragmentTestingVers = "1.2.5"
            const val espressoCoreVers = "3.3.0"
            const val archCoreTesting = "2.1.0"
            const val mockitoKotlinVers = "2.0.0"
            const val coroutinesTestVers = "1.3.9"
            const val mockServerVers = "4.9.0"
            const val baristaVers = "3.7.0"
        }

        object Kotlin {
            const val coroutinesCore =
                "org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion"
            const val coroutinesAndroid =
                "org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutinesVersion"
        }

        object Jetpack {
            const val core = "androidx.core:core-ktx:$coreVers"
            const val appCompat = "androidx.appcompat:appcompat:$appCompatVers"
            const val fragment = "androidx.fragment:fragment-ktx:$fragmentVers"
            const val rv = "androidx.recyclerview:recyclerview:$rvVers"
            const val constraint = "androidx.constraintlayout:constraintlayout:$constraintVers"
            const val palette = "androidx.palette:palette-ktx:$paletteVers"
            const val pref = "androidx.preference:preference-ktx:$preferenceVers"
            const val material = "com.google.android.material:material:$materialVers"
            const val paging = "androidx.paging:paging-runtime:$pagingVers"
            const val swipeToRefresh =
                "androidx.swiperefreshlayout:swiperefreshlayout:$swipeRefreshVers"

            val room = listOf(
                "androidx.room:room-runtime:$roomVers",
                "androidx.room:room-ktx:$roomVers"
            )

            val lifecycle by lazy {
                listOf(
                    "androidx.lifecycle:lifecycle-common-java8:$lifecycleVers",
                    "androidx.lifecycle:lifecycle-process:$lifecycleVers",
                    "androidx.lifecycle:lifecycle-livedata-ktx:$lifecycleVers",
                    "androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycleVers",
                    "androidx.savedstate:savedstate-ktx:$savedStateVers"
                )
            }
        }

        object Misc {
            const val retrofit = "com.squareup.retrofit2:retrofit:$retrofitVers"
            const val retrofitGson = "com.squareup.retrofit2:converter-gson:$retrofitVers"
            const val gson = "com.google.code.gson:gson:$gsonVers"
            const val ratingBar = "me.zhanghai.android.materialratingbar:library:$ratingBarVers"

            val okhttp by lazy {
                listOf(
                    "com.squareup.okhttp3:okhttp:$okhttpVers",
                    "com.squareup.okhttp3:logging-interceptor:$okhttpVers"
                )
            }

            val koin by lazy {
                listOf(
                    "org.koin:koin-androidx-scope:$koinVers",
                    "org.koin:koin-androidx-viewmodel:$koinVers",
                    "org.koin:koin-androidx-fragment:$koinVers"
                )
            }

            val glide = listOf(
                "com.github.bumptech.glide:glide:$glideVers",
                "com.github.bumptech.glide:recyclerview-integration:$glideVers"
            )
        }

        val unitTesting by lazy {
            listOf(
                "androidx.test:core:$testCoreVers",
                "androidx.test.ext:junit-ktx:$junitExtVers",
                "junit:junit:$junitVers",
                "org.mockito:mockito-core:$mockitoVers",
                "org.robolectric:robolectric:$roboVers",
                "org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutinesTestVers",
                "androidx.arch.core:core-testing:$archCoreTesting",
                "com.google.truth:truth:$truthVers",
                "com.nhaarman.mockitokotlin2:mockito-kotlin:$mockitoKotlinVers",
                "com.squareup.okhttp3:mockwebserver:$mockServerVers"
            )
        }

        val jvmTesting by lazy {
            listOf(
                "androidx.test:core-ktx:$testCoreVers",
                "androidx.test.ext:junit-ktx:$junitExtVers",
                "androidx.test:rules:$testCoreVers"
            )
        }

        const val barista = "com.schibsted.spain:barista:$baristaVers"

        const val fragmentTesting = "androidx.fragment:fragment-testing:$fragmentTestingVers"

        val instrumentationTesting by lazy {
            listOf(
                "androidx.test.espresso:espresso-core:$espressoCoreVers",
                "androidx.test.espresso:espresso-contrib:$espressoCoreVers",
                "androidx.test:runner:$testCoreVers",
                "androidx.test:rules:$testCoreVers",
                "androidx.test.ext:junit-ktx:$junitExtVers",
                "androidx.arch.core:core-testing:$archCoreTesting"
            )
        }

        const val espressoIdlingResource =
            "androidx.test.espresso:espresso-idling-resource:$espressoCoreVers"

        val kapts = listOf(
            "androidx.room:room-compiler:$roomVers",
            "androidx.lifecycle:lifecycle-compiler:$lifecycleVers"
        )
    }
}