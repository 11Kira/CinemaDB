package com.kira.android.cinemadb.features.account

import com.kira.android.cinemadb.model.Account

sealed class AccountState {
    data class ShowError(val error: Any): AccountState()
    data class SetAccountDetails(val accountDetails: Account): AccountState()
}