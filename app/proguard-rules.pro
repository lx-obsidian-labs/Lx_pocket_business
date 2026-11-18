-keepattributes Signature, InnerClasses, EnclosingMethod, *Annotation*
-keepclassmembers class * {
    @com.google.gson.annotations.SerializedName <fields>;
}
-keep class com.obsidianlabs.lxpocketbusiness.data.model.** { *; }
-keep class * extends androidx.room.RoomDatabase { *; }
-dontwarn org.jetbrains.annotations.**
