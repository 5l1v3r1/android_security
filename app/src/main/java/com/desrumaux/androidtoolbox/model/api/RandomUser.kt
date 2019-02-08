package com.desrumaux.androidtoolbox.model.api

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RandomUser {

    @SerializedName("results")
    @Expose
    var results: List<Result>? = null
    @SerializedName("info")
    @Expose
    var info: Info? = null

}