package com.riya.dropand.presenter

import com.riya.dropand.data.entity.Conversation
import com.riya.dropand.data.model.ConversationsStorage
import com.riya.dropand.ui.presenter.ContactChooserPresenter
import com.riya.dropand.ui.view.ContactChooserView
import com.riya.dropand.ui.viewmodel.ContactViewModel
import com.riya.dropand.ui.viewmodel.converter.ContactConverter
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.Dispatchers
import org.junit.Before
import org.junit.Test

class ContactChooserPresenterUnitTest {

    @RelaxedMockK
    private lateinit var storage: ConversationsStorage
    @RelaxedMockK
    private lateinit var view: ContactChooserView
    @RelaxedMockK
    private lateinit var converter: ContactConverter

    private lateinit var presenter: ContactChooserPresenter

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        presenter = ContactChooserPresenter(view, storage, converter, Dispatchers.Unconfined, Dispatchers.Unconfined)
    }

    @Test
    fun loading_empty() {
        coEvery { storage.getContacts() } returns ArrayList()
        presenter.loadContacts()
        verify { view.showNoContacts() }
    }

    @Test
    fun loading_notEmpty() {
        val list = arrayListOf<Conversation>(mockk())
        val viewModels = arrayListOf<ContactViewModel>(mockk())
        coEvery { storage.getContacts() } returns list
        every { converter.transform(list) } returns viewModels
        presenter.loadContacts()
        verify { view.showContacts(viewModels) }
    }
}
