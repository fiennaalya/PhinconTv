import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.DetektCreateBaselineTask

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-parcelize")
    id("kotlin-kapt")
    id("androidx.navigation.safeargs.kotlin")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
    id("io.gitlab.arturbosch.detekt")
    id("jacoco")
}

private val coverageExclusions = listOf(
    "**/R.class",
    "**/R\$*.class",
    "**/BuildConfig.*",
    "**/Manifest*.*"
)


android {
    namespace = "com.fienna.movieapp"
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
        applicationId = "com.fienna.movieapp"
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        viewBinding = true
    }

//    detekt {
//        toolVersion = "1.23.3"
//        config.setFrom(file("config/detekt/detekt.yml"))
//        buildUponDefaultConfig = true
//
//        tasks.withType<Detekt>().configureEach {
//            reports {
//                xml.required.set(true)
//                html.required.set(true)
//                txt.required.set(true)
//                sarif.required.set(true)
//                md.required.set(true)
//            }
//        }
//    }

    detekt {
        toolVersion = "1.23.3"
        buildUponDefaultConfig = true
        allRules = false
        config.setFrom("$projectDir/config/detekt/detekt.yml")
        baseline =
            file("$projectDir/config/detekt/baseline.xml")

        tasks.withType<Detekt>().configureEach {
            reports {
                html.required.set(true)
                xml.required.set(true)
                txt.required.set(true)
                sarif.required.set(true)
                md.required.set(true)
            }
        }
        tasks.withType<Detekt>().configureEach {
            jvmTarget = "17"
        }
        tasks.withType<DetektCreateBaselineTask>().configureEach {
            jvmTarget = "17"
        }
    }

}

dependencies {
    api(project(":core"))
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    //navigation
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.7")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.7")

    implementation("androidx.core:core-splashscreen:1.0.1")
    implementation("androidx.viewpager2:viewpager2:1.0.0")
    implementation("com.airbnb.android:lottie:4.1.0")
    implementation("com.facebook.shimmer:shimmer:0.5.0")
    implementation("io.insert-koin:koin-android:3.5.3")
    implementation("androidx.window:window:1.2.0")

    implementation("io.coil-kt:coil:2.5.0")

    //firebase
    implementation("com.google.firebase:firebase-database")
    debugImplementation("androidx.fragment:fragment-testing:1.6.2")

    detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.23.3")

    androidTestImplementation("androidx.test:rules:1.5.0")
    androidTestImplementation("androidx.test:runner:1.5.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation ("androidx.test.espresso:espresso-contrib:3.5.1")
    androidTestImplementation ("androidx.test.espresso:espresso-intents:3.5.1")

    testImplementation ("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.8.0")
    testImplementation("junit:junit:4.13.2")
    testImplementation("androidx.arch.core:core-testing:2.2.0")
    testImplementation("org.mockito:mockito-core:5.4.0")
    testImplementation("org.mockito:mockito-inline:3.12.4")
    testImplementation("org.mockito.kotlin:mockito-kotlin:5.0.0")
}