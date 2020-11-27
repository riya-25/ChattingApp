package com.riya.dropand.ui.view

import com.riya.dropand.data.entity.MessageFile

interface ReceivedImagesView {
    fun displayImages(imageMessages: List<MessageFile>)
    fun showNoImages()
}
