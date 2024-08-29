package com.mohit.jsingestion_webview

import android.webkit.WebView
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun ControlPanel(webView: WebView?) {
    Row (
        modifier = Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
            .border(1.dp, Color.Black, RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
    ){
        UpDown(webView)
        Feature(webView)
    }
}


@Composable
fun Feature(webView: WebView?){
    var textInput by remember { mutableStateOf("") }
    Column (modifier = Modifier
        .fillMaxSize()
        .padding(18.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = { 
            webView!!.evaluateJavascript("document.getElementsByClassName('cl-dropdown')[0].children[0].children[0].click()",null)
        }, colors = ButtonDefaults.buttonColors(
            containerColor = Color.Black
        )) {
          Text(text = "Select Region")
        }
        Row (verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween){
            TextField(
                value = textInput,
                placeholder = { Text(text = "Enter phone number") },
                onValueChange = {textInput = it},
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .height(50.dp)
                    .clip(RoundedCornerShape(10.dp)),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                )
            )
            Spacer(modifier = Modifier.width(10.dp))
            Button(onClick = {
                webView!!.evaluateJavascript("""
                    var localTextInput = document.querySelectorAll('#download-phone');
                    localTextInput.forEach(e=>e.value = ${textInput});
                """.trimIndent(),null)
            },
                modifier = Modifier.width(80.dp),
                contentPadding = PaddingValues(0.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black
                )

            ) {
                Text(text = "Paste")
            }
        }
    }
}


@Composable
fun UpDown(webView:WebView?){
    Column (modifier = Modifier.fillMaxWidth(0.2f).fillMaxHeight(),
        verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        Button(onClick = {
            webView?.evaluateJavascript("""
                    window.scrollBy({top:-400,left:0,behavior:"smooth"});
                """, null)
        },
            modifier = Modifier
                .width(50.dp)
                .height(50.dp),
            contentPadding = PaddingValues(0.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black
            ),
            elevation = ButtonDefaults.buttonElevation(10.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.KeyboardArrowUp,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(25.dp)
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        Button(onClick = {
            webView?.evaluateJavascript("""
                    window.scrollBy({top:400,left:0,behavior:"smooth"});
                """, null)
        },
            modifier = Modifier
                .width(50.dp)
                .height(50.dp),
            contentPadding = PaddingValues(0.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black
            ),
            elevation = ButtonDefaults.buttonElevation(10.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.KeyboardArrowDown,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(25.dp)
            )
        }
    }
}