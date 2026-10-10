package com.kira.android.cinemadb

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.IntOffset
import androidx.core.view.WindowCompat
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.kira.android.cinemadb.ui.component.BottomNav
import com.kira.android.cinemadb.ui.navigation.AppNavHost
import com.kira.android.cinemadb.ui.navigation.DetailsScreenRoute
import com.kira.android.cinemadb.ui.navigation.UserReviewScreenRoute
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.roundToInt

private lateinit var viewModel: MainViewModel

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val injectedViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        viewModel = injectedViewModel
        setContent {
            val view = LocalView.current
            if (!view.isInEditMode) {
                SideEffect {
                    val window = (view.context as Activity).window
                    WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars =
                        false
                }
            }
            MainScreenView()
        }
    }

    companion object {
        const val TRENDING = 1
        const val TOP_RATED = 2
    }
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MainScreenView(){
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val isDetailsScreen = currentDestination?.hasRoute<DetailsScreenRoute>() == true
    val isUserReviewScreen = currentDestination?.hasRoute<UserReviewScreenRoute>() == true
    val bottomBarOffsetHeightPx = remember { mutableFloatStateOf(0f) }

    Scaffold(
        bottomBar = {
            if (!isDetailsScreen && !isUserReviewScreen) {
                BottomNav(
                    navController = navController,
                    viewModel = viewModel,
                    modifier = Modifier.offset {
                        IntOffset(0, (-bottomBarOffsetHeightPx.floatValue).roundToInt())
                    }
                )
            }
        },
    ) { contentPadding ->
        Box(modifier = Modifier
            .background(Color.Black)
            .fillMaxSize()
        ) {
            AppNavHost(
                navController = navController,
                contentPadding = contentPadding,
                onScroll = { delta, limit ->
                    val newOffset = bottomBarOffsetHeightPx.floatValue + delta
                    bottomBarOffsetHeightPx.floatValue = newOffset.coerceIn(limit, 0f)
                }
            )
        }
    }
}