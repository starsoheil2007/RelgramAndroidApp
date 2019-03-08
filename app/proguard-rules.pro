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


-ignorewarnings
-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontpreverify
-verbose
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*



-keepattributes *Annotation*,EnclosingMethod,Signature


-keep class com.relgram.app.**  { *; }
-keep class com.atravida.**  { *; }
-keep class retrofit.** { *; }
-keep class retrofit.http.** { *; }
-keep class com.google.gson.** { *; }
-keep class com.makeramen.** { *; }
-keep class com.google.** { *; }
-keep class info.hoang8f.** { *; }

#Retrofit
-dontnote retrofit2.Platform
-dontnote retrofit2.Platform$IOS$MainThreadExecutor
-dontwarn retrofit2.Platform$Java8
-keepattributes Signature
-keepattributes Exceptions
-keep class com.squareup.** { *; }


-keepattributes Signature
-keepattributes Annotation
-keepclassmembers class okhttp3.* { *; }
-keepclassmembers class okhttp3.* { *; }
-keep interface okhttp3.* { *; }

#EventBus
-keepattributes *Annotation*
-keepclassmembers class ** {
    @org.greenrobot.eventbus.Subscribe <methods>;
}

-keep class com.nineoldandroids.animation.** { *; }
-keep interface com.nineoldandroids.animation.** { *; }
-keep class com.nineoldandroids.view.** { *; }
-keep interface com.nineoldandroids.view.** { *; }

#Orm Lite
-keep class com.j256.**
-keepclassmembers class com.j256.** { *; }
-keep enum com.j256.**
-keepclassmembers enum com.j256.** { *; }
-keep interface com.j256.**
-keepclassmembers interface com.j256.** { *; }


#YoYo
-keep class com.daimajia.androidanimations.** { *; }
-keep interface com.daimajia.androidanimations.** { *; }


#Other Libraray
-keep class com.mojtaba.** { *; }
-keep interface com.mojtaba.** { *; }


#Retrofit Sub

-dontwarn retrofit.**
-dontwarn com.squareup.okhttp.*
-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}

#Event Bus
-keepattributes *Annotation*
-keepclassmembers class * {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }

# Only required if you use AsyncExecutor
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
    <init>(java.lang.Throwable);
}
#Retrofit

-dontwarn retrofit.**
-dontwarn rx.**
-dontwarn com.squareup.okhttp.*
-keepattributes *Annotation*,Signature

-keep class com.squareup.okhttp3.** { *; }
-keep class com.squareup.retrofit2.** { *; }

-dontwarn com.squareup.okhttp.*
-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}


-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
    **[] $VALUES;
    public *;
}


#################################    Android Proguard File    ##########################################
-keepclasseswithmembernames class * {
    native <methods>;
}

# keep setters in Views so that animations can still work.
# see http://proguard.sourceforge.net/manual/examples.html#beans
-keepclassmembers public class * extends android.view.View {
   void set*(***);
   *** get*();
}

# We want to keep methods in Activity that could be used in the XML attribute onClick
-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}

# For enumeration classes, see http://proguard.sourceforge.net/manual/examples.html#enumerations
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keepclassmembers class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator CREATOR;
}

-keepclassmembers class **.R$* {
    public static <fields>;
}


-dontwarn android.support.**