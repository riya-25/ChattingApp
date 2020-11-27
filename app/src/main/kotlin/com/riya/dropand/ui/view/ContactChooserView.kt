package com.riya.dropand.ui.view

import com.riya.dropand.ui.viewmodel.ContactViewModel

interface ContactChooserView {
    fun showContacts(contacts: List<ContactViewModel>)
    fun showNoContacts()
}
