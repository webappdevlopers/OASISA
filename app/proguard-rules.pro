# General configuration
-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontpreverify
-verbose

# Obfuscation configuration
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile
-keepattributes *Annotation*

# Google Play Services configuration
-keep class com.google.android.gms.** { *; }
-dontwarn com.google.android.gms.**

# Support library configuration
-keep class android.support.v4.** { *; }
-dontwarn android.support.v4.**

# Your application classes configuration
-keep class com.webapp.oasis.** { *; }
-keep class org.json.** { *; }

