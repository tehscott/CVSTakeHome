package com.stromberg.cvstakehome.model

import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NavType
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class FlickrResponse(
    val items: List<FlickrItem>,
) : Parcelable

@Parcelize
data class FlickrItem(
    val title: String,
    val link: String,
    val media: Media,
    val published: String,
    val tags: String,
) : Parcelable

@Parcelize
data class Media(
    @SerializedName("m")
    val url: String,
) : Parcelable

class FlickrItemType : NavType<FlickrItem>(isNullableAllowed = false) {
    override fun get(bundle: Bundle, key: String): FlickrItem? {
        return bundle.getParcelable(key)
    }
    override fun parseValue(value: String): FlickrItem {
        return Gson().fromJson(value, FlickrItem::class.java)
    }
    override fun put(bundle: Bundle, key: String, value: FlickrItem) {
        bundle.putParcelable(key, value)
    }
}