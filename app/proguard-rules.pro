# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in E:\AppData\Local\Android\Sdk/tools/proguard/proguard-android.txt
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
# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\As\sdk/tools/proguard/proguard-android.txt
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
# 代码混淆压缩比，在0~7之间，默认为5,一般不下需要修改
-optimizationpasses 5

# 混淆时不使用大小写混合，混淆后的类名为小写
# windows下的同学还是加入这个选项吧(windows大小写不敏感)
-dontusemixedcaseclassnames

# 指定不去忽略非公共的库的类的成员
-dontskipnonpubliclibraryclassmembers

# 指定不去忽略非公共的库的类
-dontskipnonpubliclibraryclasses
-keepattributes *Annotation*,EnclosingMethod,Signature
-keepattributes InnerClasses
-dontoptimize

#java8
-dontwarn java.lang.invoke.*
-dontwarn **$$Lambda$*
#---------------------------------1.实体类---------------------------------
-keep class com.wezley.uploadImage.model.** { *; }


# 保持测试相关的代码
-dontnote junit.framework.**
-dontnote junit.runner.**
-dontwarn android.test.**
-dontwarn android.support.test.**
-dontwarn org.junit.*




#---------------------------------2.第三方包---------------------------------
-dontwarn android.support.v4.**
-keep class android.support.v4.** {*;}
-dontwarn javax.naming.**
-dontwarn org.bouncycastle.util.**



#okhttp
-dontwarn okhttp3.**
-keep class okhttp3.**{*;}

#okio
-dontwarn okio.**
-keep class okio.**{*;}



#okhttp
-dontwarn com.squareup.okhttp3.**
-keep class com.squareup.okhttp3.** { *;}
-keep class okhttp3.** { *;}
-keep class okio.** { *;}
-dontwarn sun.security.**
-keep class sun.security.** { *;}
-dontwarn okio.**
-dontwarn okhttp3.**





#universal-image-loader
-keep class com.nostra13.universalimageloader.** { *; }
-dontwarn com.nostra13.universalimageloader.**


# 这类数据不应该混淆
#  需要反射的代码
#  系统接口
#  Jni接口
#  需要序列号和反序列化的代码（即实现Serializable接口的JavaBean）
#  与服务端进行元数据交互的JavaBean（JSON、XML中对应的类）

-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

#butterknife
-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$ViewBinder { *; }
-keepnames class *{ @butterknife.InjectView *;}
-dontwarn butterknife.Views$InjectViewProcessor

-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}

-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}


#nineoldandroids
#-dontwarn com.nineoldandroids.*
#-keep class com.nineoldandroids.** { *;}




# universalimageloader
-dontwarn com.nostra13.universalimageloader.**
-keep class com.nostra13.universalimageloader.** { *; }

#bugly
-dontwarn com.tencent.bugly.**
-keep public class com.tencent.bugly.**{ *;}

#-------------------------------------------------------------------------

#---------------------------------3.与js互相调用的类------------------------



#-------------------------------------------------------------------------

#---------------------------------4.反射相关的类和方法-----------------------


#--------------------------------5. jni相关类和方法--------------------------



#---------------------------------------------------------------------------
#----------------------------------------------------------------------------
#-------------------------------------------基本不用动区域--------------------
#---------------------------------基本指令区----------------------------------
-optimizationpasses 5
-dontskipnonpubliclibraryclassmembers
-printmapping proguardMapping.txt
-optimizations !code/simplification/cast,!field/*,!class/merging/*
-keepattributes *Annotation*,InnerClasses
# 过滤泛型
-keepattributes Signature
-keepattributes SourceFile,LineNumberTable
# 反射类不进行混淆
#-keep class com.test.model.response.** {*;}

#-ignorewarnings
#----------------------------------------------------------------------------

#---------------------------------默认保留区---------------------------------
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class * extends android.view.View
-keep public class com.android.vending.licensing.ILicensingService
-keep class android.support.** {*;}
-keep public class * extends android.os.IInterface

#保持自定义组件不被混淆
-keep public class * extends android.view.View {
    *** get*();
    void set*(***);
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
    public void *(android.view.View);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}
-keep class **.R$* {
    *;
}
-keepclassmembers class * {
    void *(**On*Event);
}

# jni方法
-keepclasseswithmembernames class * { # 保持 native 方法不被混淆
    native <methods>;
}

-keepclassmembers class * extends android.app.Activity { # 保持自定义控件类不被混淆
    public void *(android.view.View);
}

-keepclassmembers enum * { # 保持枚举 enum 类不被混淆
public static **[] values();
public static ** valueOf(java.lang.String);
}

-keep class * implements android.os.Parcelable { # 保持 Parcelable 不被混淆
public static final android.os.Parcelable$Creator *;
}

#不混淆R类里及其所有内部static类中的所有static变量字段
-keepclassmembers class **.R$* {
    public static <fields>;
}

#----------------------------------------------------------------------------

#---------------------------------webview------------------------------------
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
#-keepclassmembers class * extends android.webkit.webViewClient {
#   public void *(android.webkit.WebView, java.lang.String, android.graphics.Bitmap);
#  public boolean *(android.webkit.WebView, java.lang.String);
#}
#-keepclassmembers class * extends android.webkit.webViewClient {
#   public void *(android.webkit.webView, jav.lang.String);
#}
#-------------------------------------------------
#-------------------------------------------------

