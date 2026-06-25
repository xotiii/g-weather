plugins {
	alias(libs.plugins.android.application)
	alias(libs.plugins.kotlin.compose)
	alias(libs.plugins.dagger.hilt.android)
	alias(libs.plugins.ksp)
	alias(libs.plugins.kotlin.serialization)
}

android {
	namespace = "com.dcchua.gweather"
	compileSdk {
		version = release(37) {
			minorApiLevel = 0
		}
	}

	defaultConfig {
		applicationId = "com.dcchua.gweather"
		minSdk = 33
		targetSdk = 37
		versionCode = 1
		versionName = "1.0"

		testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
	}

	buildTypes {
		release {
			optimization {
				enable = false
			}
		}
	}
	compileOptions {
		sourceCompatibility = JavaVersion.VERSION_17
		targetCompatibility = JavaVersion.VERSION_17
	}
	buildFeatures {
		compose = true
	}
}

dependencies {

	// Compose
	implementation(platform(libs.androidx.compose.bom))
	implementation(libs.androidx.activity.compose)
	implementation(libs.androidx.compose.material3)
	implementation(libs.androidx.compose.material3.adaptive.navigation.suite)
	implementation(libs.androidx.compose.ui)
	implementation(libs.androidx.compose.ui.graphics)
	implementation(libs.androidx.compose.ui.tooling.preview)

	// Navigation
	implementation(libs.androidx.navigation.compose)

	// Android
	implementation(libs.androidx.core.ktx)
	implementation(libs.androidx.lifecycle.runtime.ktx)

	// Networks
	implementation(libs.retrofit2.retrofit)
	implementation(libs.retrofit2.converter.gson)
	implementation(libs.okhttp3.okhttp)
	implementation(libs.okhttp3.logging.interceptor)

	// Dependency Injection
	implementation(libs.dagger.hilt.android)
	ksp(libs.dagger.hilt.compiler)
	testImplementation(libs.dagger.hilt.android.testing)
	androidTestAnnotationProcessor(libs.dagger.hilt.compiler)
	implementation(libs.androidx.hilt.navigation.compose)

	// Serialization
	implementation(libs.kotlin.serialization)

	// Room
	implementation(libs.androidx.room.runtime)
	implementation(libs.androidx.room.ktx)
	ksp(libs.androidx.room.compiler)

	// Location
	implementation(libs.google.android.gms.location)
	implementation(libs.coroutines.play.services)

	// Test
	testImplementation(libs.junit)
	androidTestImplementation(platform(libs.androidx.compose.bom))
	androidTestImplementation(libs.androidx.compose.ui.test.junit4)
	androidTestImplementation(libs.androidx.espresso.core)
	androidTestImplementation(libs.androidx.junit)
	debugImplementation(libs.androidx.compose.ui.test.manifest)
	debugImplementation(libs.androidx.compose.ui.tooling)
}