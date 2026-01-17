package com.attendance.facerec.data.local.converter

import androidx.room.TypeConverter
import com.attendance.facerec.domain.model.GeoLocation
import com.google.gson.Gson

class GeoLocationConverter {
    private val gson = Gson()

    @TypeConverter
    fun fromGeoLocation(location: GeoLocation?): String? {
        return location?.let { gson.toJson(it) }
    }

    @TypeConverter
    fun toGeoLocation(json: String?): GeoLocation? {
        return json?.let { gson.fromJson(it, GeoLocation::class.java) }
    }
}
