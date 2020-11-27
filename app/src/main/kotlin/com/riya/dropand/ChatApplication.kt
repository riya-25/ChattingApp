package com.riya.dropand

import android.app.Activity
import android.app.Application
import android.content.res.Configuration
import android.os.StrictMode
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.NightMode
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import com.crashlytics.android.Crashlytics
import com.riya.dropand.data.model.BluetoothConnector
import com.riya.dropand.data.model.ProfileManager
import com.riya.dropand.data.model.UserPreferences
import com.riya.dropand.di.*
import com.riya.dropand.ui.activity.ChatActivity
import com.riya.dropand.ui.activity.ConversationsActivity
import com.riya.dropand.ui.util.StartStopActivityLifecycleCallbacks
import com.riya.dropand.ui.util.ThemeHolder
import com.kobakei.ratethisapp.RateThisApp
import io.fabric.sdk.android.Fabric
import org.koin.android.ext.android.getKoin
import org.koin.android.ext.android.inject
import org.koin.android.ext.android.startKoin
import org.koin.core.scope.Scope

class ChatApplication : Application(), LifecycleObserver, ThemeHolder {

    var isConversationsOpened = false
    var currentChat: String? = null

    @NightMode
    private var nightMode: Int = AppCompatDelegate.MODE_NIGHT_NO

    private val connector: BluetoothConnector by inject()
    private val profileManager: ProfileManager by inject()
    private val preferences: UserPreferences by inject()

    private lateinit var localeSession: Scope

    override fun onCreate() {
        super.onCreate()

        if (!BuildConfig.DEBUG) {
            Fabric.with(this, Crashlytics())
        }

        startKoin(this, listOf(applicationModule,
                bluetoothConnectionModule, databaseModule, localStorageModule, viewModule))
        localeSession = getKoin().createScope(localeScope)

        nightMode = preferences.getNightMode()

        ProcessLifecycleOwner.get().lifecycle.addObserver(this)

        registerActivityLifecycleCallbacks(object : StartStopActivityLifecycleCallbacks() {

            override fun onActivityStarted(activity: Activity?) {
                when (activity) {
                    is ConversationsActivity -> isConversationsOpened = true
                    is ChatActivity -> currentChat =
                            activity.intent.getStringExtra(ChatActivity.EXTRA_ADDRESS)
                }
            }

            override fun onActivityStopped(activity: Activity?) {
                when (activity) {
                    is ConversationsActivity -> isConversationsOpened = false
                    is ChatActivity -> currentChat = null
                }
            }
        })

        val config = RateThisApp.Config().apply {
            setTitle(R.string.rate_dialog__title)
            setMessage(R.string.rate_dialog__message)
            setYesButtonText(R.string.rate_dialog__rate)
            setNoButtonText(R.string.rate_dialog__no)
            setCancelButtonText(R.string.rate_dialog__cancel)
        }
        RateThisApp.init(config)

        if (BuildConfig.DEBUG) {

            StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .build())
            StrictMode.setVmPolicy(StrictMode.VmPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .build())
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        localeSession.close()
        localeSession = getKoin().createScope(localeScope)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    internal fun prepareConnection() {
        if (!profileManager.getUserName().isEmpty()) {
            connector.prepare()
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    internal fun releaseConnection() {
        connector.release()
    }

    override fun setNightMode(@NightMode nightMode: Int) {
        this.nightMode = nightMode
    }

    override fun getNightMode() = nightMode
}
