package v.kira.cinemadb.features.account

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import v.kira.cinemadb.util.SettingsPrefs
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    @ApplicationContext val context : Context,
    private val useCase: AccountUseCase
): ViewModel() {

    private val mutableAccountState: MutableSharedFlow<AccountState> = MutableSharedFlow()
    val accountState = mutableAccountState.asSharedFlow()

    lateinit var header: String

    init {
        viewModelScope.launch(Dispatchers.IO) {
            header =  SettingsPrefs(context).getToken.first().toString()
        }
    }

    fun getAccountDetails(accountId: Long) {
        viewModelScope.launch(CoroutineExceptionHandler { _, error ->
            runBlocking {
                mutableAccountState.emit(AccountState.ShowError(error))
            }
        }) {
            val result = useCase.getAccountDetails(header, accountId)
            mutableAccountState.emit(AccountState.SetAccountDetails(result))
        }
    }
}