package com.kira.android.cinemadb.ui.component

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.toFontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.kira.android.cinemadb.MainViewModel
import com.kira.android.cinemadb.R
import com.kira.android.cinemadb.ui.navigation.BottomMenuItem

@Composable
fun BottomNav(
    navController: NavController,
    viewModel: MainViewModel,
    modifier: Modifier
) {
    var selectedItem by remember { mutableStateOf("movies") }
    val selectedTab by remember { mutableStateOf(viewModel.currentlySelectedTab) }

    val screens = listOf(
        BottomMenuItem.Movies,
        BottomMenuItem.TV,
        BottomMenuItem.Watchlist
    )
    Surface(
        color = Color.DarkGray,
        modifier = modifier
    ) {
        BottomNavigation(
            modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars),
            backgroundColor = Color.DarkGray,
            elevation = 0.dp
        ) {
            screens.forEach {
                BottomNavigationItem(
                    selected = (selectedItem == selectedTab.collectAsState().value),
                    onClick = {
                        selectedItem = it.label
                        navController.navigate(it.route) {
                            navController.graph.startDestinationRoute?.let { screenRoute ->
                                popUpTo(screenRoute) {
                                    saveState = true
                                }
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                        viewModel.updateSelectedTab(it.label)
                    },
                    label = {
                        Text(
                            text = it.label,
                            color = if (selectedTab.collectAsState().value == it.label) Color.White else Color.Gray,
                            fontSize = 10.sp,
                            fontFamily = if (selectedItem == selectedTab.collectAsState().value) Font(
                                R.font.roboto_bold
                            ).toFontFamily() else Font(R.font.roboto_medium).toFontFamily(),
                        )
                    },
                    icon = {
                        Icon(
                            imageVector = ImageVector.vectorResource(it.icon),
                            contentDescription = it.label,
                            tint = if (selectedTab.collectAsState().value == it.label) Color.White else Color.Gray
                        )
                    }
                )
            }
        }
    }
}