package com.istekno.gohipeandroidapp.utility

import android.net.Uri
import com.istekno.gohipeandroidapp.retrofit.EngineerModelResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.net.URI.create
import java.net.URLConnection


class GetURLFromResources {
    fun getURLForResource(resourceId: Int): String {
        return Uri.parse("android.resource://com.istekno.gohipeandroidapp/$resourceId").toString()
    }

    fun prepareFilePart(partName: String, fileUri: String): MultipartBody.Part {
        val file = File(fileUri)
        val mimeType: String = URLConnection.guessContentTypeFromName(file.name)
        val requestFile: RequestBody = RequestBody.create(mimeType.toMediaTypeOrNull(), file)
        return MultipartBody.Part.createFormData(partName, file.name, requestFile)
    }

//    private fun updateEngAvatar(data: EngineerModelResponse) {
//        coroutineScope.launch {
//            val getURLImg = "src\\main\\res\\drawable\\ic_avatar_en.png"
//            val requestBody = RequestBody.create("image/jpeg".toMediaTypeOrNull(), File(getURLImg))
//
//            imageName = MultipartBody.Part.createFormData("image", File(getURLImg).name, requestBody)
//
//            withContext(Dispatchers.IO) {
//                try {
//                    service.updateEngineerAvatar(data.enID!!, imageName)
//                } catch (e: Throwable) {
//                    e.printStackTrace()
//                }
//            }
//        }
//    }
}