plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("kotlin-parcelize")
    id ("kotlin-kapt")
    id("jacoco")
}

private val coverageExclusions = listOf(
    "**/R.class",
    "**/R\$*.class",
    "**/BuildConfig.*",
    "**/Manifest*.*"
)

android {
    namespace = "com.fienna.movieapp.core"
    compileSdk = 34
    buildFeatures.buildConfig = true

    configure<JacocoPluginExtension> {
        toolVersion = "0.8.10"
    }

    val jacocoTestReport = tasks.create("jacocoTestReport")
    androidComponents.onVariants { variant ->
        val testTaskName = "test${variant.name.capitalize()}UnitTest"

        val reportTask =
            tasks.register("jacoco${testTaskName.capitalize()}Report", JacocoReport::class) {
                dependsOn(testTaskName)

                reports {
                    html.required.set(true)
                }

                classDirectories.setFrom(
                    fileTree("$buildDir/tmp/kotlin-classes/${variant.name}") {
                        exclude(coverageExclusions)
                    }
                )

                sourceDirectories.setFrom(
                    files("$projectDir/src/main/java")
                )
                executionData.setFrom(file("$buildDir/jacoco/$testTaskName.exec"))
            }

        jacocoTestReport.dependsOn(reportTask)
    }

    tasks.withType<Test>().configureEach {
        configure<JacocoTaskExtension> {
            isIncludeNoLocationClasses = true
            excludes = listOf("jdk.internal.*")
        }
    }


    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")

        buildConfigField("String", "BEARER_TOKEN", "\"eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI0ODcwZGU2Y2E2YzNjMjQyMDVlMjFjMmJhZmYyYWE5ZiIsInN1YiI6IjY1OGE4MmZmZGQyNTg5NzFhZTZiZmYxNCIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.UvkVcTG4Z3eSL5EGuSuyc0uLZGfjNRpZRc0LBBh_k2o\"")
        buildConfigField("String", "BASE_URL", "\"https://api.themoviedb.org/3/\"")
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {

    api("androidx.core:core-ktx:1.12.0")
    api("androidx.appcompat:appcompat:1.6.1")
    api("com.google.android.material:material:1.11.0")
    api("androidx.databinding:viewbinding:8.2.2")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    api("androidx.core:core-splashscreen:1.0.1")
    api("com.squareup.retrofit2:retrofit:2.9.0")
    api("com.squareup.retrofit2:converter-gson:2.9.0")
    api("com.squareup.okhttp3:logging-interceptor:4.11.0")
    //debugImplementation("com.github.chuckerteam.chucker:library:3.5.2")
    api ("io.insert-koin:koin-android:3.5.3")
    debugImplementation("com.github.chuckerteam.chucker:library:4.0.0")
    releaseImplementation("com.github.chuckerteam.chucker:library-no-op:4.0.0")

    api("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
    api("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")

    api("androidx.navigation:navigation-fragment-ktx:2.7.7")
    api("androidx.navigation:navigation-ui-ktx:2.7.7")

    api("androidx.room:room-runtime:2.6.1")
    api ("androidx.room:room-ktx:2.6.1")
    kapt ("androidx.room:room-compiler:2.6.1")
    api ("androidx.paging:paging-runtime-ktx:3.2.1")
    api("androidx.room:room-paging:2.6.1")


    api(platform("com.google.firebase:firebase-bom:32.7.2"))
    api("com.google.firebase:firebase-crashlytics-ktx")
    api("com.google.firebase:firebase-analytics-ktx")
    api("com.google.firebase:firebase-config-ktx")
    api("com.google.firebase:firebase-messaging-ktx")
    api("com.google.firebase:firebase-auth-ktx")
    api ("com.google.firebase:firebase-database-ktx")
}