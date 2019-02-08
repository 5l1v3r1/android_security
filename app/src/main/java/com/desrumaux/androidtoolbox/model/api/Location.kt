package com.desrumaux.androidtoolbox.model.api

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Location {

    @SerializedName("street")
    @Expose
    var street: String = ""
    @SerializedName("city")
    @Expose
    var city: String = ""
    @SerializedName("state")
    @Expose
    var state: String = ""
    @SerializedName("postcode")
    @Expose
    var postcode: String = ""
    @SerializedName("coordinates")
    @Expose
    var coordinates: Coordinates? = null
    @SerializedName("timezone")
    @Expose
    var timezone: Timezone? = null

    override fun toString(): String {
        return street + " " + city.toUpperCase()
    }
}