package com.istekno.gohipeandroidapp.models

import com.google.gson.annotations.SerializedName

data class ImagePickerModel(
    @SerializedName("imageid")
    val imageID: String,

    @SerializedName("imagename")
    val imageName: String
)