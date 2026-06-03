plugins {
	id("com.android.application")
	id("kotlin-android")
}

android {
	namespace = "com.jadx.app"
	compileSdk = 35
	
	configurations {
		all {
			exclude(group = "com.android.tools.lint", module = "lint-gradle")
		}
	}
	
	defaultConfig {
		applicationId = "com.jadx.app"
		minSdk = 26
		targetSdk = 35
		versionCode = 1
		versionName = "1.0.0"
		
		testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
	}
	
	buildFeatures {
		viewBinding = true
		compose = true
	}
	
	composeOptions {
		kotlinCompilerExtensionVersion = "1.5.14"
	}
	
	buildTypes {
		release {
			isMinifyEnabled = true
			proguardFiles(
				getDefaultProguardFile("proguard-android-optimize.txt"),
				"proguard-rules.pro"
			)
		}
	}
	
	compileOptions {
		sourceCompatibility = JavaVersion.VERSION_11
		targetCompatibility = JavaVersion.VERSION_11
	}
	
	kotlinOptions {
		jvmTarget = "11"
	}
}

dependencies {
	// JADX Core
	implementation(project(":jadx-core"))
	implementation(project(":jadx-plugins-tools"))
	implementation(project(":jadx-commons:jadx-app-commons"))
	
	// JADX Input Plugins
	implementation(project(":jadx-plugins:jadx-dex-input"))
	implementation(project(":jadx-plugins:jadx-java-input"))
	implementation(project(":jadx-plugins:jadx-smali-input"))
	implementation(project(":jadx-plugins:jadx-aab-input"))
	implementation(project(":jadx-plugins:jadx-apkm-input"))
	implementation(project(":jadx-plugins:jadx-apks-input"))
	
	// Android Core
	implementation("androidx.core:core-ktx:1.13.1")
	implementation("androidx.appcompat:appcompat:1.7.0")
	implementation("androidx.activity:activity-ktx:1.9.0")
	implementation("androidx.activity:activity-compose:1.9.0")
	
	// Material Design
	implementation("com.google.android.material:material:1.12.0")
	
	// Compose
	implementation(platform("androidx.compose:compose-bom:2024.06.00"))
	implementation("androidx.compose.ui:ui")
	implementation("androidx.compose.ui:ui-graphics")
	implementation("androidx.compose.ui:ui-tooling-preview")
	implementation("androidx.compose.material3:material3")
	implementation("androidx.compose.foundation:foundation")
	implementation("androidx.compose.material:material-icons-extended")
	
	// File Management
	implementation("androidx.documentfile:documentfile:1.0.1")
	
	// Storage
	implementation("androidx.security:security-crypto:1.1.0-alpha06")
	
	// Coroutines
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.1")
	
	// Logging
	implementation("ch.qos.logback:logback-android:0.1.5")
	
	// Testing
	testImplementation("junit:junit:4.13.2")
	androidTestImplementation("androidx.test.ext:junit:1.1.5")
	androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
	androidTestImplementation(platform("androidx.compose:compose-bom:2024.06.00"))
	androidTestImplementation("androidx.compose.ui:ui-test-junit4")
	
	debugImplementation("androidx.compose.ui:ui-tooling")
	debugImplementation("androidx.compose.ui:ui-test-manifest")
}
