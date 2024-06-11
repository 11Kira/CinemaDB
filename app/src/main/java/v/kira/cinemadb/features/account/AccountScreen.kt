package v.kira.cinemadb.features.account

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import kotlinx.coroutines.flow.SharedFlow
import v.kira.cinemadb.model.Account
import v.kira.cinemadb.util.AppUtil

lateinit var viewModel: AccountViewModel

@Composable
fun AccountScreen() {
    viewModel = hiltViewModel()
    MainScreen(viewModel.accountState)
    viewModel.getAccountDetails(7749280)
}

@Composable
fun MainScreen(sharedFlow: SharedFlow<AccountState>) {
    val lifecycleOwner = LocalLifecycleOwner.current
    var userAccount by remember { mutableStateOf<Account?>(null) }

    LaunchedEffect(key1 = Unit) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            sharedFlow.collect { state ->
                when (state) {
                    is AccountState.SetAccountDetails -> {
                        userAccount = state.accountDetails
                    }
                    is AccountState.ShowError -> {
                        Log.e("Error: ", state.error.toString())
                    }
                }
            }
        }
    }
    userAccount?.let { SetupAccountScreen(account = it) }
}

@Composable
fun SetupAccountScreen(account: Account) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .verticalScroll(rememberScrollState())
    ) {
        val posterPath = AppUtil.retrievePosterImageUrl(account.avatar.tmdb.avatarPath)
        Box(
            modifier = Modifier
                .width(250.dp)
                .height(250.dp)
                .align(Alignment.CenterHorizontally)
                .padding(10.dp)
                .clip(CircleShape),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current).data(posterPath).crossfade(true).build(),
                contentDescription = "Avatar",
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            )
        }
        Text(
            textAlign = TextAlign.Center,
            color = Color.White,
            text = account.username,
            fontWeight = FontWeight.Medium,
            fontSize = 20.sp
        )
    }
}