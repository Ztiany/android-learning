package com.ztiany.webview.https;

import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.ztiany.webview.R;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;


/**
 * @author Ztiany
 *         Email: ztiany3@gmail.com
 *         Date : 2017-08-31 18:13
 */
public class HttpsWebViewActivity extends AppCompatActivity {

    private static final String TAG = HttpsWebViewActivity.class.getSimpleName();

    private WebView mWebView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_web_view);
        mWebView = (WebView) findViewById(R.id.web_view);
        setupWebView();
        setWebClient();
        mWebView.loadUrl("http://www.12306.cn/mormhweb/");
    }

    //如果需要做证书校验，参考：https://stackoverflow.com/questions/33825696/android-error-in-webview-loadurl-trust-anchor-for-certification-path-not-fou
    private void setWebClient() {
        mWebView.setWebViewClient(new WebViewClient() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl(request.getUrl().toString());
                return true;
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            //设置WebView接受所有网站的证书
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                Log.d(TAG, "onReceivedSslError() called with: view = [" + view + "], handler = [" + handler + "], error = [" + error + "]");
                //不要调用super的方法
                // handler.cancel();// Android默认的处理方式
                // handleMessage(Message msg);// 进行其他处理
                handler.proceed();// 接受所有网站的证书
            }
        });
    }

    //在Android 5.0上 WebView 默认不允许加载 Http 与 Https 混合内容：
    private void setupWebView() {

        //在Android5.0中，WebView方面做了些修改，如果你的系统target api为21以上:
        //系统默认禁止了mixed content和第三方cookie。可以使用setMixedContentMode() 和 setAcceptThirdPartyCookies()以分别启用。
        //系统现在可以智能选择HTML文档的portion来绘制。这种新特性可以减少内存footprint并改进性能。若要一次性渲染整个HTML文档，可以调用这个方法enableSlowWholeDocumentDraw()
        //如果你的app的target api低于21:系统允许mixed content和第三方cookie，并且总是一次性渲染整个HTML文档。

        /*
        MIXED_CONTENT_ALWAYS_ALLOW：允许从任何来源加载内容，即使起源是不安全的；5.0以下
        MIXED_CONTENT_NEVER_ALLOW：不允许Https加载Http的内容，即不允许从安全的起源去加载一个不安全的资源；
        MIXED_CONTENT_COMPATIBILITY_MODE：当涉及到混合式内容时，WebView 会尝试去兼容最新Web浏览器的风格
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mWebView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

        // android 5.0以上默认不支持Mixed Content
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mWebView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE);
        }
    }
}
