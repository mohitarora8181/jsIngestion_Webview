package com.mohit.jsingestion_webview

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.view.ViewGroup
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.mohit.jsingestion_webview.ui.theme.JsIngestionWebViewTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            JsIngestionWebViewTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Home(Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@SuppressLint("SetJavascriptEnabled")
@Composable
fun Home(modifier: Modifier){
    var webView by remember { mutableStateOf<WebView?>(null) }
    var cangoback by remember { mutableStateOf(false) }
    var dialogOpen by remember { mutableStateOf(false) }


    if (dialogOpen) {
        AlertDialog(
            onDismissRequest = { dialogOpen = false },
            confirmButton = { Button(onClick = { dialogOpen = false}) {
                Text(text = "OK")
            }},
            title = { Text(text = "Sorry !!")},
            text = { Text(text = "Login not supported this time")}
        )
    }

    Column (modifier = modifier.fillMaxSize()) {
        AndroidView(
            factory = {
                WebView(it).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    settings.javaScriptEnabled = true
                    webView = this
                    webViewClient = object :WebViewClient() {
                        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                            if (!cangoback) {
                                cangoback = view?.canGoBack() ?: false
                            }
                        }

                        override fun onPageFinished(view: WebView?, url: String?) {
                            webView!!.evaluateJavascript(
                                """
                                document.getElementsByTagName('main')[0].style.overflowX = 'hidden';
                                const login = document.getElementById("menu-item-4081").childNodes[0];
                                login.addEventListener("click",()=>{
                                    Android.onLoginClick();
                                });
                                """, null
                            )
                            super.onPageFinished(view, url)
                        }
                    }

                    addJavascriptInterface(object :Any(){
                        @JavascriptInterface
                        fun onLoginClick(){
                            dialogOpen = true
                        }
                    },"Android")
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.8f),
            update = {
               it.loadUrl("https://zebpay.com/in/")
            }
        )
        ControlPanel(webView = webView)
    }

    BackHandler (enabled = cangoback) {
       if(webView!!.canGoBack()){
           webView!!.goBack()
       }
    }
}