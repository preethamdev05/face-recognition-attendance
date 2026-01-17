-keep class com.attendance.facerec.** { *; }
-keep class com.google.mlkit.** { *; }
-keep class org.tensorflow.** { *; }
-keep class com.google.firebase.** { *; }
-keep interface com.google.firebase.** { *; }

-dontwarn com.google.android.gms.**
-dontwarn com.google.firebase.**
-dontwarn org.tensorflow.**

-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile

# For data classes
-keep class com.attendance.facerec.data.model.** { *; }
-keep class com.attendance.facerec.domain.model.** { *; }

# Keep Firebase auth models
-keep class com.google.firebase.auth.** { *; }
-keep class com.google.firebase.database.** { *; }

# For Room
-keep @androidx.room.Entity class * { *; }
-keep @androidx.room.Dao interface * { *; }

# For Hilt
-keep class **_Factory { *; }
-keep class **_Binding { *; }
-keep class dagger.hilt.** { *; }
