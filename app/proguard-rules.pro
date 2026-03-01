# ─── kotlinx.serialization ────────────────────────────────────────────────────
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.AnnotationsKt

-keepclassmembers class kotlinx.serialization.json.** {
    *** Companion;
}
-keepclasseswithmembers class kotlinx.serialization.json.** {
    kotlinx.serialization.KSerializer serializer(...);
}

# Keep data model classes used for JSON deserialization
-keep class com.saurabh.marathicalendar.data.model.** { *; }
-keepclassmembers class com.saurabh.marathicalendar.data.model.** {
    <fields>;
    <init>(...);
}

# ─── Enums (FontSizeOption stored by ordinal in SharedPreferences) ─────────────
-keepclassmembers enum com.saurabh.marathicalendar.** {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# ─── Kotlin Coroutines ────────────────────────────────────────────────────────
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}
-dontwarn kotlinx.coroutines.**

# ─── Jetpack Compose ──────────────────────────────────────────────────────────
-keep class androidx.compose.** { *; }
-dontwarn androidx.compose.**

# ─── Lifecycle / ViewModel ────────────────────────────────────────────────────
-keep class androidx.lifecycle.** { *; }
-keepclassmembers class * extends androidx.lifecycle.ViewModel {
    <init>(...);
}

# ─── Keep line numbers for crash reports ──────────────────────────────────────
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile
