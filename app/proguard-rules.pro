# Production-Grade ProGuard Rules for Face Recognition Attendance System
# DO NOT use blanket -keep rules. Be specific.

# === DEBUGGING ATTRIBUTES ===
# Keep line numbers for crash reports
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile

# Keep annotations for reflection
-keepattributes *Annotation*
-keepattributes Signature
-keepattributes Exceptions

# === ROOM DATABASE ===
# Keep Room entities (data classes with @Entity)
-keep @androidx.room.Entity class * { *; }
-keep @androidx.room.Dao interface * { *; }

# Specifically keep our Room entities
-keep class com.attendance.facerec.data.local.entity.** { *; }

# === DOMAIN MODELS ===
# Keep domain models used in Firebase and serialization
-keep class com.attendance.facerec.domain.model.** { *; }

# === HILT DEPENDENCY INJECTION ===
# Keep Hilt-generated classes
-keep class **_Factory { *; }
-keep class **_MembersInjector { *; }
-keep class dagger.hilt.** { *; }
-keep class javax.inject.** { *; }

# Keep ViewModel constructors for Hilt injection
-keep class * extends androidx.lifecycle.ViewModel {
    <init>(...);
}
-keepclassmembers class * extends androidx.lifecycle.ViewModel {
    <init>(...);
}

# === FIREBASE ===
# Keep Firebase model classes (if used directly)
-keep class com.google.firebase.** { *; }
-keep interface com.google.firebase.** { *; }

# Keep classes that implement Parcelable or Serializable (for Firebase)
-keepclassmembers class * implements android.os.Parcelable {
    static ** CREATOR;
}
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    !static !transient <fields>;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

# === ML KIT ===
# Keep ML Kit classes
-keep class com.google.mlkit.** { *; }
-keep interface com.google.mlkit.** { *; }

# === TENSORFLOW LITE ===
# Keep TensorFlow Lite classes
-keep class org.tensorflow.** { *; }
-keep interface org.tensorflow.** { *; }

# === RETROFIT & OKHTTP ===
# Keep Retrofit service interfaces
-keep interface * {
    @retrofit2.http.* <methods>;
}

# Keep OkHttp platform used by Retrofit
-dontwarn okhttp3.**
-dontwarn okio.**
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }

# === GSON ===
# Keep Gson classes
-keep class com.google.gson.** { *; }
-keepclassmembers class * {
    @com.google.gson.annotations.SerializedName <fields>;
}

# === GLIDE ===
# Keep Glide generated API
-keep public class * extends com.bumptech.glide.module.AppGlideModule
-keep class com.bumptech.glide.GeneratedAppGlideModuleImpl

# === KOTLIN COROUTINES ===
# Keep coroutines
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}

# === WARNINGS SUPPRESSION ===
# Suppress warnings for third-party libraries
-dontwarn com.google.android.gms.**
-dontwarn com.google.firebase.**
-dontwarn org.tensorflow.**
-dontwarn javax.annotation.**
-dontwarn javax.inject.**
-dontwarn sun.misc.Unsafe

# === OPTIMIZATION ===
# Enable aggressive optimization
-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-verbose

# Remove logging in release builds
-assumenosideeffects class android.util.Log {
    public static *** d(...);
    public static *** v(...);
    public static *** i(...);
}
