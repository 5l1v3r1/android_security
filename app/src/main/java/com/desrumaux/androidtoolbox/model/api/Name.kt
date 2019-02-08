package com.desrumaux.androidtoolbox.model.api

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Name {

    @SerializedName("title")
    @Expose
    var title: String = ""
    @SerializedName("first")
    @Expose
    var first: String = ""
    @SerializedName("last")
    @Expose
    var last: String = ""

    override fun toString(): String {
        return last.toUpperCase() + " " + first
    }
}