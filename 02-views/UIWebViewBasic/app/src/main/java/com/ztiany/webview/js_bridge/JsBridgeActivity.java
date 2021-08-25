package com.ztiany.webview.js_bridge;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;

import com.ztiany.webview.R;

import java.util.Arrays;

import androidx.appcompat.app.AppCompatActivity;

public class JsBridgeActivity extends AppCompatActivity {

    private static final String TAG = JsBridgeActivity.class.getSimpleName();
    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_js_bridge);
        initWebView();
        initAction();
    }

    private void initAction() {
        final EditText et = (EditText) findViewById(R.id.et_content);

        findViewById(R.id.btn_call_js).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = et.getText().toString();
                String method = "addElement";
                if (TextUtils.isEmpty(content)) {
                    method = method.concat("()");
                } else {
                    method = method.concat("(\"").concat(content).concat("\")");
                }
                Log.d(TAG, method);
                mWebView.loadUrl("javascript:" + method);
            }
        });
    }

    private void initWebView() {
        mWebView = (WebView) findViewById(R.id.web_view);

        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setDomStorageEnabled(true);

        mWebView.setWebChromeClient(mWebChromeClient);
        mWebView.setWebViewClient(mWebViewClient);
        mWebView.loadUrl("file:///android_asset/js_bridge.html");
    }

    private WebViewClient mWebViewClient = new WebViewClient() {

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            //Utils.webViewLoadLocalJs(view, "client.js");
        }
    };


    private WebChromeClient mWebChromeClient = new WebChromeClient() {

        @Override
        public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
            Log.d(TAG, "onJsConfirm() called with: view = [" + view + "], url = [" + url + "], message = [" + message + "], result = [" + result + "]");
            if (handCallJavaMethod(view, url, message, result)) {
                result.confirm();
                return true;
            }
            return super.onJsConfirm(view, url, message, result);
        }
    };

    public static final String JS_DIVIDER = "#A#";
    public static final String JS_FLAG = "callAndroid";

    private boolean handCallJavaMethod(WebView view, String url, String message, JsResult result) {
        if (TextUtils.isEmpty(message)) {
            return false;
        }
        if (!message.startsWith(JS_FLAG)) {
            return false;
        }
        String[] split = message.split(JS_DIVIDER);
        Log.d(TAG, "split:" + Arrays.toString(split));
        return true;
    }

}
