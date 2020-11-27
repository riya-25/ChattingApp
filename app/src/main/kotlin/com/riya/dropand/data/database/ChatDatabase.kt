package com.riya.dropand.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.riya.dropand.data.entity.ChatMessage
import com.riya.dropand.data.entity.Conversation

@Database(entities = [(ChatMessage::class), (Conversation::class)], version = 3)
@TypeConverters(Converter::class)
abstract class ChatDatabase: RoomDatabase() {
    abstract fun conversationsDao(): ConversationsDao
    abstract fun messagesDao(): MessagesDao
}
