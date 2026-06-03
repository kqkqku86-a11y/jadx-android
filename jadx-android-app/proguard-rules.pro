# JADX-specific rules
-keep class jadx.** { *; }
-keepclassmembers class jadx.** { *; }

# Keep all plugin classes
-keep class jadx.plugins.** { *; }
-keepclassmembers class jadx.plugins.** { *; }

# Kotlin
-keepclassmembers class kotlin.** {
	 native <methods>;
}

# Android
-keep public class android.** { *; }

# Compose
-keep interface androidx.compose.** { *; }
-keep class androidx.compose.** { *; }

# Libraries
-keep class ch.qos.logback.** { *; }
-keep class org.slf4j.** { *; }
