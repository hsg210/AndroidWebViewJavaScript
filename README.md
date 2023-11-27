# AndroidWebViewJavaScript
@javascriptinterface

AndroidWebView &amp; JavaScript interActive

網路上一些範例已太舊
重新用Android Studio Giraffe | 2022.3.1 Patch 1 開新專案執行
並透過此範例了解Android的@javascriptinterface
怎麼透過webview的html呼叫Android 內的對應函數設計


compileSdk = 33


defaultConfig {
    applicationId = "com.example.androidwebviewjavascript"
    minSdk = 28
    targetSdk = 33
    versionCode = 1
    versionName = "1.0"

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
}
  
dependencies {

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}
