package com.kira.android.cinemadb.features.splash

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalResources
import androidx.lifecycle.lifecycleScope
import coil3.compose.AsyncImage
import com.kira.android.cinemadb.MainActivity
import com.kira.android.cinemadb.R
import com.kira.android.cinemadb.utils.SettingsPrefs
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout

@AndroidEntryPoint
class LoadingActivity: ComponentActivity() {

    val token = "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiIxMmJlYWE5ZjdhYzAwN2YwMDg4ZDBmOWM1NzZjZmU4NCIsInN1YiI6IjVhNTU5ZGUxYzNhMzY4NWVlNjAxYjU0ZSIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.A_W6hgICqOtrDQl_CfvLEZ8XFeOJ7cnKQZ-_ablYfQY"
    val header = "Bearer $token"
    val accountId = 7749280

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LoadingScreenView()
        }
        lifecycleScope.launch {
            withTimeout(5000) {
                val intent = Intent(this@LoadingActivity, MainActivity::class.java)
                SettingsPrefs(this@LoadingActivity).setAccountId(accountId.toLong())
                SettingsPrefs(this@LoadingActivity).setToken(header)
                startActivity(intent)
                finish()
            }
        }
    }
}

@Composable
fun LoadingScreenView() {
    Box(modifier = Modifier
        .fillMaxSize()
        .background(color = Color.DarkGray)) {
        AsyncImage(
            model = LocalResources.current.getDrawable(R.drawable.logo),
            contentDescription = "Poster",
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .align(Alignment.Center)
        )
    }
}