package v.kira.cinemadb

import android.content.Context
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    @ApplicationContext val context: Context
): ViewModel() {

    private var _currentlySelectedTab = MutableStateFlow("Movies")
    val currentlySelectedTab: StateFlow<String> = _currentlySelectedTab.asStateFlow()

    fun updateSelectedTab(selectedTab: String) {
        _currentlySelectedTab.value = selectedTab
    }
}