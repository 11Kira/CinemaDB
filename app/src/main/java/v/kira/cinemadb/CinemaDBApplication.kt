package v.kira.cinemadb

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import v.kira.cinemadb.util.SettingsPrefs

@HiltAndroidApp
class CinemaDBApplication: Application() {

    val token = "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiIxMmJlYWE5ZjdhYzAwN2YwMDg4ZDBmOWM1NzZjZmU4NCIsInN1YiI6IjVhNTU5ZGUxYzNhMzY4NWVlNjAxYjU0ZSIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.A_W6hgICqOtrDQl_CfvLEZ8XFeOJ7cnKQZ-_ablYfQY"
    val header = "Bearer $token"

    override fun onCreate() {
        super.onCreate()
        applicationScope.launch {
            SettingsPrefs(applicationContext).setToken(header)
        }
    }
    companion object {
        var applicationScope = MainScope()
    }
}