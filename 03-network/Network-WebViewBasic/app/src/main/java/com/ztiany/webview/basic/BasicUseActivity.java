package com.ztiany.webview.basic;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.HttpAuthHandler;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.ztiany.webview.R;
import com.ztiany.webview.utils.Network;
import com.ztiany.webview.utils.WebViewUtils;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

/**
 * WebView基础用法
 *
 * @author Ztiany
 *         Email: ztiany3@gmail.com
 *         Date : 2017-08-31 16:37
 */
public class BasicUseActivity extends AppCompatActivity {

    private static final String TAG = BasicUseActivity.class.getSimpleName();

    /*
    Android的WebView在低版本和高版本采用了不同的webkit版本内核，4.4后直接使用了Chrome。
     */
    private WebView mWebView;
    private WebSettings webSettings;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_web_view);
        mWebView = (WebView) findViewById(R.id.web_view);
        webSettings = mWebView.getSettings();

        setupWebViewBase();
        setupStorage();
        setLBS();
        setupJs();
        setWebClient();
        setWebChromeClient();

        mWebView.loadUrl("http://3g.163.com/");
    }

    @Override
    protected void onResume() {
        super.onResume();
        mWebView.onResume();
        //恢复pauseTimers时的动作。
        mWebView.resumeTimers();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mWebView.onPause();
        //当应用程序被切换到后台我们使用了webview，
        //这个方法不仅仅针对当前的webview而是全局的全应用程序的webview，
        //它会暂停所有webview的layout，parsing，javascripttimer。降低CPU功耗。
        mWebView.pauseTimers();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        WebViewUtils.destroy(mWebView);
    }

    ///////////////////////////////////////////////////////////////////////////
    // 前进后退
    ///////////////////////////////////////////////////////////////////////////
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        /*
        goBack()//后退
        goForward()//前进
        goBackOrForward(intSteps) //以当前的index为起始点前进或者后退到历史记录中指定的steps，如果steps为负数则为后退，正数则为前进
        canGoForward()//是否可以前进
        canGoBack() //是否可以后退
         */
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    ///////////////////////////////////////////////////////////////////////////
    // WebChromeClient
    ///////////////////////////////////////////////////////////////////////////

    /*
       WebChromeClient 主要辅助 WebView 处理J avaScript 的对话框、网站 Logo、网站 title、load 进度等处理:
        WebView 只是用来处理一些 html 的页面内容，只用 WebViewClient 就行了，如果需要更丰富的处理效果，比如 JS、进度条等，就要用到 WebChromeClient
    */
    private void setWebChromeClient() {
        mWebView.setWebChromeClient(new WebChromeClient() {

            //(关闭WebView)
            @Override
            public void onCloseWindow(WebView window) {
                super.onCloseWindow(window);
            }

            @Override
            public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
                return super.onCreateWindow(view, isDialog, isUserGesture, resultMsg);
            }

            //处理alert弹出框，html 弹框的一种方式
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                return super.onJsAlert(view, url, message, result);
            }

            //处理prompt弹出框，通过result设置输入信息
            @Override
            public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
                return super.onJsPrompt(view, url, message, defaultValue, result);
            }

            //处理confirm弹出框
            @Override
            public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
                return super.onJsConfirm(view, url, message, result);
            }

            //获得网页的加载进度，显示在右上角的TextView控件中，总进度为100
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                Log.d(TAG, "onProgressChanged() called with: view = [" + view + "], newProgress = [" + newProgress + "]");
            }

            @Override
            public void onReceivedIcon(WebView view, Bitmap icon) {
                super.onReceivedIcon(view, icon);
            }

            //获取Web页中的title用来设置自己界面中的title
            //当加载出错的时候，比如无网络，这时onReceiveTitle中获取的标题为 找不到该网页,
            //因此建议当触发onReceiveError时，不要使用获取到的title
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
            }

            @Override
            public void onShowCustomView(View view, CustomViewCallback callback) {
                super.onShowCustomView(view, callback);
            }
        });
    }


    ///////////////////////////////////////////////////////////////////////////
    // WebViewClient
    ///////////////////////////////////////////////////////////////////////////

    //WebViewClient 主要辅助WebView执行处理各种响应请求事件的
    private void setWebClient() {
        mWebView.setWebViewClient(new WebViewClient() {

            // 在加载页面资源时会调用，每一个资源（比如图片）的加载都会调用一次。
            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
            }

            //这个事件就是开始载入页面调用的，我们可以设定一个loading的页面，告诉用户程序在等待网络响应。
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            //这个事件就是开始载入页面调用的，我们可以设定一个loading的页面，告诉用户程序在等待网络响应。
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }

            // (报告错误信息)
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
            }

            ////重写此方法可以让WebView处理https请求。
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                super.onReceivedSslError(view, handler, error);
            }

            //（获取返回信息授权请求）
            @Override
            public void onReceivedHttpAuthRequest(WebView view, HttpAuthHandler handler, String host, String realm) {
                super.onReceivedHttpAuthRequest(view, handler, host, realm);
            }

            //打开网页时不调用系统浏览器,而是在本WebView中显示
            //在网页上的所有加载都经过这个方法,这个函数我们可以做很多操作。
            //比如获取url，查看url.contains(“add”)，进行添加操作
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            //重写此方法才能够处理在浏览器中的按键事件。
            @Override
            public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
                return super.shouldOverrideKeyEvent(view, event);
            }

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                // return super.shouldOverrideUrlLoading(view, request);
                view.loadUrl(request.getUrl().toString());
                return true;
            }

            // 拦截替换网络请求数据,  API 11开始引入，API 21弃用
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
                return super.shouldInterceptRequest(view, url);
            }

            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                return super.shouldInterceptRequest(view, request);
            }

            //(更新历史记录)
            @Override
            public void doUpdateVisitedHistory(WebView view, String url, boolean isReload) {
                super.doUpdateVisitedHistory(view, url, isReload);
            }

            //(应用程序重新请求网页数据)
            @Override
            public void onFormResubmission(WebView view, Message dontResend, Message resend) {
                super.onFormResubmission(view, dontResend, resend);
            }

            // (WebView发生改变时调用)
            @Override
            public void onScaleChanged(WebView view, float oldScale, float newScale) {
                super.onScaleChanged(view, oldScale, newScale);
            }

            //（Key事件未被加载时调用）
            @Override
            public void onUnhandledKeyEvent(WebView view, KeyEvent event) {
                super.onUnhandledKeyEvent(view, event);
            }
        });
    }


    ///////////////////////////////////////////////////////////////////////////
    // 基础设置
    ///////////////////////////////////////////////////////////////////////////

    private void setLBS() {
        //打开 WebView 的 LBS 功能，这样 JS 的 geolocation 对象才可以使用
        webSettings.setGeolocationEnabled(true);// 启用地理定位
        //webSettings.setGeolocationDatabasePath("");// 设置定位的数据库路径
    }

    @SuppressWarnings("all")
    private void setupStorage() {

        //开启 Application Caches 功能
        webSettings.setAppCacheEnabled(true);
        //设置ApplicationCaches缓存目录， 每个Application只调用一次WebSettings.setAppCachePath()，
        webSettings.setAppCachePath(getExternalCacheDir().getAbsolutePath());

        webSettings.setDatabaseEnabled(true);//开启 database storage API 功能，默认值为false
        webSettings.setDomStorageEnabled(true); // 开启 DOM storage API 功能，这样 JS 的 localStorage,sessionStorage 对象才可以使用
        //设置是否打开 WebView 表单数据的保存功能
        webSettings.setSaveFormData(true);
        webSettings.setAllowFileAccess(true);  //设置可以访问文件

        /*
        缓存模式：
        - LOAD_CACHE_ONLY: 不使用网络，只读取本地缓存数据
        - LOAD_DEFAULT: （默认）根据cache-control决定是否从网络上取数据。
        - LOAD_NO_CACHE: 不使用缓存，只从网络获取数据.
        - LOAD_CACHE_ELSE_NETWORK，只要本地有，无论是否过期，或者no-cache，都使用缓存中的数据。
         */
        if (Network.isConnected(getApplicationContext())) {
            webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);//根据cache-control决定是否从网络上取数据。
        } else {
            webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);//没网，则从本地获取，即离线加载
        }

        //mWebView.clearCache(true);//清除网页访问留下的缓存，由于内核缓存是全局的因此这个方法不仅仅针对webview而是针对整个应用程序.
        //mWebView.clearHistory();//清空历史记录，这个方法要在 onPageFinished() 的方法之后调用。
        //mWebView.clearFormData();//这个api仅仅清除自动完成填充的表单数据，并不会清除WebView存储到本地的数据。
    }

    /*
     Using setJavaScriptEnabled can introduce XSS vulnerabilities into your application, review carefully.
     Your code should not invoke setJavaScriptEnabled if you are not sure that your app really requires JavaScript support
    */
    @SuppressLint("SetJavaScriptEnabled")
    private void setupJs() {
        //设置了这个属性后我们才能在 WebView 里与我们的 Js 代码进行交互，对于 WebApp 是非常重要的，默认是 false，
        //因此我们需要设置为 true，这个本身会有漏洞，具体的下面我会讲到
        webSettings.setJavaScriptEnabled(true);
        //设置 JS 是否可以打开 WebView 新窗口
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);

    }


    private void setupWebViewBase() {
        //WebView 是否支持多窗口，如果设置为 true，需要重写
        //WebChromeClient#onCreateWindow(WebView, boolean, boolean, Message) 函数，默认为 false
        webSettings.setSupportMultipleWindows(false);


        //这个属性用来设置 WebView 是否能够加载图片资源，需要注意的是，这个方法会控制所有图片，包括那些使用 data URI 协议嵌入
        //的图片。使用 setBlockNetworkImage(boolean) 方法来控制仅仅加载使用网络 URI 协议的图片。需要提到的一点是如果这
        //个设置从 false 变为 true 之后，所有被内容引用的正在显示的 WebView 图片资源都会自动加载，该标识默认值为 true。
        webSettings.setLoadsImagesAutomatically(true);
        //标识是否加载网络上的图片（使用 http 或者 https 域名的资源），需要注意的是如果 getLoadsImagesAutomatically()
        //不返回 true，这个标识将没有作用。这个标识和上面的标识会互相影响。
        webSettings.setBlockNetworkImage(false);//可用于优化加载速度：首先阻塞图片，让图片不显示，页面加载好以后，在放开图片


        //设置 WebView 的默认 userAgent 字符串
        webSettings.setUserAgentString("android");


        //设置是否 WebView 支持 “viewport” 的 HTML meta tag，这个标识是用来屏幕自适应的，当这个标识设置为 false 时，
        //页面布局的宽度被一直设置为 CSS 中控制的 WebView 的宽度；如果设置为 true 并且页面含有 viewport meta tag，那么
        //被这个 tag 声明的宽度将会被使用，如果页面没有这个 tag 或者没有提供一个宽度，那么一个宽型 viewport 将会被使用。
        webSettings.setUseWideViewPort(true);
        // 缩放至屏幕的大小
        webSettings.setLoadWithOverviewMode(true);//设置自适应屏幕，两者合用


        //设置 WebView 的字体，可以通过这个函数，改变 WebView 的字体，默认字体为 "sans-serif"
        webSettings.setStandardFontFamily("sans-serif");
        //设置 WebView 字体的大小，默认大小为 16
        webSettings.setDefaultFontSize(20);
        //设置 WebView 支持的最小字体大小，默认为 8
        webSettings.setMinimumFontSize(12);


        //设置页面是否支持缩放(前提)
        webSettings.setSupportZoom(false);
        //显示WebView提供的缩放控件
        webSettings.setDisplayZoomControls(false);//隐藏原生的缩放控件
        webSettings.setBuiltInZoomControls(false);//设置内置的缩放控件。若为false，则该WebView不可缩放
        //设置文本的缩放倍数，默认为 100
        webSettings.setTextZoom(100);


        //提高渲染的优先级
        webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);


        //用来控制html的布局，总共有三种类型：
        //NORMAL：正常显示，没有渲染变化。
        //SINGLE_COLUMN：把所有内容放到WebView组件等宽的一列中
        //NARROW_COLUMNS：可能的话，使所有列的宽度不超过屏幕宽度。
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);


        //当webView调用requestFocus时为webView设置节点
        webSettings.setNeedInitialFocus(true);


        webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式
    }
}
