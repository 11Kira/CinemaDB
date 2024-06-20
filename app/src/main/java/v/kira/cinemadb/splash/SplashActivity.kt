package v.kira.cinemadb.splash

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import coil.compose.AsyncImage
import coil.request.ImageRequest
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import v.kira.cinemadb.MainActivity
import v.kira.cinemadb.R
import v.kira.cinemadb.util.SettingsPrefs

@AndroidEntryPoint
class LoadingActivity: ComponentActivity() {

    val token = "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiIxMmJlYWE5ZjdhYzAwN2YwMDg4ZDBmOWM1NzZjZmU4NCIsInN1YiI6IjVhNTU5ZGUxYzNhMzY4NWVlNjAxYjU0ZSIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.A_W6hgICqOtrDQl_CfvLEZ8XFeOJ7cnKQZ-_ablYfQY"
    val header = "Bearer $token"
    val accountId = 7749280

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        setContent {
            LoadingScreenView()
        }
        lifecycleScope.launch {
            splashScreen.setKeepOnScreenCondition { true }
            SettingsPrefs(this@LoadingActivity).setAccountId(accountId.toLong())
            SettingsPrefs(this@LoadingActivity).setToken(header)
            val intent = Intent(this@LoadingActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}

@Composable
fun LoadingScreenView() {
    Box(modifier = Modifier.fillMaxSize().background(color = Color.DarkGray)) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current).data(LocalContext.current.resources.getDrawable(R.drawable.logo)).crossfade(true).build(),
            contentDescription = "Poster",
            modifier = Modifier.fillMaxWidth().wrapContentHeight().align(Alignment.Center)
        )
    }
}