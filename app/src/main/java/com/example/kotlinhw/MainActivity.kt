package com.example.kotlinhw

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Surface
import androidx.compose.material.MaterialTheme
import com.example.kotlinhw.ui.theme.KotlinhwTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KotlinhwTheme {
                Surface(color = MaterialTheme.colors.background) {
                    UserSurveyScreen()
                }
            }
        }
    }
}
