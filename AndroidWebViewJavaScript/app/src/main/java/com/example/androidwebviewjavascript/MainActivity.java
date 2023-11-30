package com.example.androidwebviewjavascript;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LifecycleRegistry;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    private static final String JS_NAME = "JsTest";
    private String Tag = "MainActivity";
    private Toolbar mToolbar;
    private Button bCallJs;
    private WebView webView;
    private boolean lockBackKey;
    private LifecycleRegistry mLifecycleRegistry;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(mToolbar);

        bCallJs = findViewById(R.id.callJs);
        bCallJs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*
                AlertDialog.Builder builder = new AlertDialog.Builder(getBaseContext());
                builder.setMessage("This is a message.");
                builder.setTitle("This is a title.");
                AlertDialog dialog = builder.create();
                dialog.show();*/

                webView.post(new Runnable() {
                    @Override
                    public void run() {
                        webView.loadUrl("javascript:calledFromAndroid('msg from Android Java')");
                    }
                });
            }
        });

        webView = findViewById(R.id.webview);
        WebSettings webSettings = webView.getSettings();
        // 設定與Js互動的權限
        webSettings.setJavaScriptEnabled(true);
        // 設定允許JS彈窗
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webView.addJavascriptInterface(new JsInterface(this), JS_NAME);
        // 步驟1：載入JS程式碼
        // 格式規定為:file:///android_asset/檔案名稱.html
        webView.loadUrl("file:///android_asset/jsinteract.html");
        mLifecycleRegistry = new LifecycleRegistry(MainActivity.this);


    }

    @Override
    public void onBackPressed() {
        if (lockBackKey) {
            Toast.makeText(this, "Back key was locked", Toast.LENGTH_SHORT).show();
        } else {
            super.onBackPressed();
        }
    }

    public class JsInterface {
        private static final String TAG = "JsInterface";
        private Context mContext;
        public JsInterface(Context context) {
            mContext = context;
        }

        @JavascriptInterface
        public String getAndroidVersion() {
            return Build.VERSION.RELEASE;
        }
        @JavascriptInterface
        public String getDeviceInfo() {
            JSONObject deviceInfo = new JSONObject();
            try {
                deviceInfo.put("model", Build.MODEL);
                deviceInfo.put("androidVersion", Build.VERSION.RELEASE);
                deviceInfo.put("manufacturer", Build.MANUFACTURER);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return deviceInfo.toString();
        }
        @JavascriptInterface
        public void logJson(String json) {
            Log.d(TAG, "logJson Received JSON: " + json);
            try {
                JSONObject jsonObject = new JSONObject(json);
                String name = jsonObject.optString("name");
                int age = jsonObject.optInt("age");
                String city = jsonObject.optString("city");

                // 將鍵和值存儲到相應的變數中
                String nameVariable = name;
                int ageVariable = age;
                String cityVariable = city;

                Log.d(TAG, "Received JSON - Name: " + nameVariable + ", Age: " + ageVariable + ", City: " + cityVariable);
            } catch (JSONException e) {
                Log.e(TAG, "Error parsing JSON: " + e.getMessage());
            }
        }
        @JavascriptInterface
        public void showToast(String msg) {
            Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
        }

        @JavascriptInterface
        public void showToolbar(final boolean showIt) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mToolbar.setVisibility(showIt ? View.VISIBLE : View.GONE);
                    Toast.makeText(mContext, "Toolbar="+showIt, Toast.LENGTH_SHORT).show();
                }
            });
        }

        @JavascriptInterface
        public void openAndroidDialog(String title, String msg) {
/*            try {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setMessage(msg)
                        .setTitle(title);
                AlertDialog dialog = builder.create();
                dialog.show();
            } catch (Exception e) {
                Log.e("openAndroidDialog", "Exception: " + e.getMessage());
                Toast.makeText(mContext, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
 */
            //必須在主線程中調用，因為它會更新 UI,使用runOnUiThread()方法將handleLifecycleEvent()  方法的調用放到主線程中
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setMessage(msg)
                            .setTitle(title);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                    } catch (Exception e) {
                        Log.e("openAndroidDialog", "Exception: " + e.getMessage());
                        Toast.makeText(mContext, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        @JavascriptInterface
        public void lockBackKey(boolean lockIt) {
            lockBackKey = lockIt;
            Toast.makeText(mContext, "lockBackKey="+lockIt, Toast.LENGTH_SHORT).show();
        }
    }
}
