package com.moment.studywithme

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.moment.studywithme.ui.theme.SWMTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SWMTheme {
                SWMApp(application as SWMApplication)
            }
        }
    }
}
