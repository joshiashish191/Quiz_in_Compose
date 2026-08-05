# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# ---------- Rules for libraries used in this project ----------

# Room: keep generated schema and DAO implementations
-keep class androidx.room.** { *; }
-keepclassmembers class * {
    @androidx.room.* <fields>;
    @androidx.room.* <methods>;
}

# Gson: keep model classes used via reflection
-keep class com.google.gson.** { *; }
-keepattributes Signature
-keepattributes *Annotation*

# Jetpack Compose: keep composable methods and runtime
-keepclassmembers class * {
    @androidx.compose.runtime.Composable <methods>;
}
-keep class androidx.compose.runtime.** { *; }

# Keep Kotlin metadata and standard library reflections
-keep class kotlin.Metadata { *; }

# Keep model classes that may be serialized/deserialized (adjust package as needed)
-keepclassmembers class net.softglobe.quizincompose.** { *; }

# Reduce warnings for third-party libs you know are safe
-dontwarn okio.**
-dontwarn kotlinx.coroutines.**

# NOTE: Adjust the keep rules to be as narrow as possible for better obfuscation.
# Test the release build and upload mapping files to your crash reporting tool.
