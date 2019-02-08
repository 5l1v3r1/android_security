package com.desrumaux.androidtoolbox.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class User(
    @SerializedName("firstName")
    @Expose
    var firstName: String?,
    @SerializedName("lastName")
    @Expose
    var lastName: String?,
    @SerializedName("birthDate")
    @Expose
    var birthDate: String?
)