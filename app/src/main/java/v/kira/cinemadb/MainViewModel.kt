package v.kira.cinemadb

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    @ApplicationContext val context: ApplicationContext
): ViewModel() {

    private var _currentlySelectedTab = MutableStateFlow(0)
    val currentlySelectedTab: StateFlow<Int> = _currentlySelectedTab.asStateFlow()

    fun updateSelectedTab(selectedTab: Int) {
        _currentlySelectedTab.value = selectedTab
    }
}