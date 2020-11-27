package com.riya.dropand.data.model

import android.net.Uri

interface FileManager {
    suspend fun extractApkFile(): Uri?
}
