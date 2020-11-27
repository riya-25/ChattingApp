package com.riya.dropand.ui.viewmodel.converter

import com.amulyakhare.textdrawable.TextDrawable
import com.riya.dropand.data.entity.Conversation
import com.riya.dropand.ui.viewmodel.ContactViewModel
import com.riya.dropand.utils.getFirstLetter

class ContactConverter {

    fun transform(conversation: Conversation): ContactViewModel {
        return ContactViewModel(
                conversation.deviceAddress,
                "${conversation.displayName} (${conversation.deviceName})",
                TextDrawable.builder()
                        .buildRound(conversation.displayName.getFirstLetter(), conversation.color)
        )
    }

    fun transform(conversationCollection: Collection<Conversation>): List<ContactViewModel> {
        return conversationCollection.map {
            transform(it)
        }
    }
}
