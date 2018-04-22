# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in ./android-sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

-keepattributes Exceptions

#okhttp
-keep class okhttp3.** { *; }
-dontwarn okhttp3.*

-keep class com.google.inject.** { *; }
-keep class org.apache.http.** { *; }
-keep class org.apache.james.mime4j.** { *; }
-keep class javax.inject.** { *; }

#Widgets
-keep class org.adw.library.widgets.** { *; }

# Parse
-keep class com.parse.** { *; }
-dontwarn com.parse.**
-keepattributes SourceFile,LineNumberTable
-keepnames class com.parse.** { *; }
-dontwarn com.squareup.**
-dontwarn android.net.SSLCertificateSocketFactory
-dontwarn android.app.Notification
-keep class bolts.** { *; }
-keepnames class bolts.** { *; }

-dontwarn com.squareup.okhttp.**

-keep class android.support.v7.widget.SearchView { *; }

-keep class com.crashlytics.** { *; }
-dontwarn com.crashlytics.**

-dontnote okio.**,com.mopub.** ,com.twitter.sdk.android.**
-dontnote com.rengwuxian.materialedittext.** ,com.google.android.gms.** ,com.google.firebase.**

#SpringFramework
-keepclassmembers class org.nuvola.mobile.prixpascher.business.** { *; }
-keepclassmembers class org.nuvola.mobile.prixpascher.dto.** { *; }
-keepclassmembers class org.nuvola.mobile.prixpascher.models.** { *; }
-keepclassmembers enum * { *; }

-keep class com.fasterxml.jackson.annotation.** { *; }
-dontwarn com.fasterxml.jackson.databind.**

-dontwarn org.springframework.**
-keep class org.springframework.** { *; }
-keep class org.codehaus.jackson.** { *; }

#Font detection
-keep class .R
-keep class **.R$* {
    <fields>;
}

-dontwarn com.onesignal.**

# These 2 methods are called with reflection.
-keep class com.google.android.gms.common.api.GoogleApiClient {
    void connect();
    void disconnect();
}


-keep public interface android.app.OnActivityPausedListener {*;}
-keep class com.onesignal.ActivityLifecycleListenerCompat** {*;}


# Observer backcall methods are called with reflection
-keep class com.onesignal.OSSubscriptionState {
    void changed(com.onesignal.OSPermissionState);
}

-keep class com.onesignal.OSPermissionChangedInternalObserver {
    void changed(com.onesignal.OSPermissionState);
}

-keep class com.onesignal.OSSubscriptionChangedInternalObserver {
    void changed(com.onesignal.OSSubscriptionState);
}

-keep class ** implements com.onesignal.OSPermissionObserver {
    void onOSPermissionChanged(com.onesignal.OSPermissionStateChanges);
}

-keep class ** implements com.onesignal.OSSubscriptionObserver {
    void onOSSubscriptionChanged(com.onesignal.OSSubscriptionStateChanges);
}

-keep class com.onesignal.shortcutbadger.impl.AdwHomeBadger { <init>(...); }
-keep class com.onesignal.shortcutbadger.impl.ApexHomeBadger { <init>(...); }
-keep class com.onesignal.shortcutbadger.impl.AsusHomeLauncher { <init>(...); }
-keep class com.onesignal.shortcutbadger.impl.DefaultBadger { <init>(...); }
-keep class com.onesignal.shortcutbadger.impl.EverythingMeHomeBadger { <init>(...); }
-keep class com.onesignal.shortcutbadger.impl.HuaweiHomeBadger { <init>(...); }
-keep class com.onesignal.shortcutbadger.impl.LGHomeBadger { <init>(...); }
-keep class com.onesignal.shortcutbadger.impl.NewHtcHomeBadger { <init>(...); }
-keep class com.onesignal.shortcutbadger.impl.NovaHomeBadger { <init>(...); }
-keep class com.onesignal.shortcutbadger.impl.OPPOHomeBader { <init>(...); }
-keep class com.onesignal.shortcutbadger.impl.SamsungHomeBadger { <init>(...); }
-keep class com.onesignal.shortcutbadger.impl.SonyHomeBadger { <init>(...); }
-keep class com.onesignal.shortcutbadger.impl.VivoHomeBadger { <init>(...); }
-keep class com.onesignal.shortcutbadger.impl.XiaomiHomeBadger { <init>(...); }
-keep class com.onesignal.shortcutbadger.impl.ZukHomeBadger { <init>(...); }


-dontwarn com.amazon.**

# Proguard ends up removing this class even if it is used in AndroidManifest.xml so force keeping it.
-keep public class com.onesignal.ADMMessageHandler {*;}

-keep class com.onesignal.JobIntentService$* {*;}
