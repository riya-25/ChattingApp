package com.riya.dropand.di

import com.riya.dropand.data.database.Database
import com.riya.dropand.data.model.*
import com.riya.dropand.ui.view.NotificationView
import com.riya.dropand.ui.view.NotificationViewImpl
import com.riya.dropand.ui.viewmodel.converter.ChatMessageConverter
import com.riya.dropand.ui.viewmodel.converter.ContactConverter
import com.riya.dropand.ui.viewmodel.converter.ConversationConverter
import com.riya.dropand.ui.widget.ShortcutManager
import com.riya.dropand.ui.widget.ShortcutManagerImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module.module

val bluetoothConnectionModule = module {
    single { BluetoothConnectorImpl(androidContext()) as BluetoothConnector }
    factory { BluetoothScannerImpl(androidContext()) as BluetoothScanner }
}

val databaseModule = module {
    single { Database.getInstance(androidContext()) }
    single { MessagesStorageImpl(get()) as MessagesStorage }
    single { ConversationsStorageImpl(get()) as ConversationsStorage }
}

val localStorageModule = module {
    single { FileManagerImpl(androidContext()) as FileManager }
    single { UserPreferencesImpl(androidContext()) as UserPreferences }
    single { ProfileManagerImpl(androidContext()) as ProfileManager }
}

const val localeScope = "locale_scope"

val viewModule = module {
    single { NotificationViewImpl(androidContext()) as NotificationView }
    single { ShortcutManagerImpl(androidContext()) as ShortcutManager }
    scope(localeScope) { ContactConverter() }
    scope(localeScope) { ConversationConverter(androidContext()) }
    scope(localeScope) { ChatMessageConverter(androidContext()) }
}
