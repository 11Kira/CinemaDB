package v.kira.cinemadb.features.account

import v.kira.cinemadb.model.Account

sealed class AccountState {
    data class ShowError(val error: Any): AccountState()
    data class SetAccountDetails(val accountDetails: Account): AccountState()
}