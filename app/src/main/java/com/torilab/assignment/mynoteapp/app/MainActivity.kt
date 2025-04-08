package com.torilab.assignment.mynoteapp.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.torilab.assignment.designsystem.MyNoteAppTheme
import com.torilab.assignment.mynoteapp.app.navigation.RootNavigation

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            MyNoteAppTheme {
                RootNavigation()
            }
        }
    }
}
