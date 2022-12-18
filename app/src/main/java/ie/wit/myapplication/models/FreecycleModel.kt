package ie.wit.myapplication.models

import android.os.Parcelable
import com.google.firebase.database.Exclude
import kotlinx.parcelize.Parcelize
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

@Parcelize
data class FreecycleModel(
    var uid: String? = "",
    var name: String = "",
    var contactNumber: String = "",
    var listingTitle: String = "",
    var listingDescription: String = "",
    var image: String = "",
    var itemAvailable: Boolean = true,
    var dateAvailable: LocalDate = LocalDate.now(),
    var email: String? = "joe@bloggs.com",
    var profilePic: String? = "",
    var location : Location? = Location()
) : Parcelable {


    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "uid" to uid,
            "name" to name,
            "contactNumber" to contactNumber,
            "listingTitle" to listingTitle,
            "listingDescription" to listingDescription,
            "image" to image,
            "location" to location,
            "itemAvailable" to itemAvailable,
            "dateAvailable" to dateAvailable.atStartOfDay(ZoneId.systemDefault()).toInstant()
                .toEpochMilli(),
            "email" to email,
            "profilePic" to profilePic
        )
    }

    companion object {
        @Exclude
        fun fromMap(map: Map<String, Any?>): FreecycleModel {
            var location: Location? = null
            if(map["location"] != null) {
                location = getLocationFromMap(map["location"] as Map<String, Any?>)
            }
            return FreecycleModel(
                uid = map["uid"].toString(),
                name = map["name"].toString(),
                contactNumber = map["contactNumber"].toString(),
                listingTitle = map["listingTitle"].toString(),
                listingDescription = map["listingDescription"].toString(),
                image = map["image"].toString(),
                location = location,
                itemAvailable = map["itemAvailable"] as Boolean,
                dateAvailable = Instant.ofEpochMilli(map["dateAvailable"] as Long)
                    .atZone(ZoneId.systemDefault()).toLocalDate(),
                email = map["email"].toString(),
                profilePic = map["profilePic"].toString()
            )
        }

        private fun getLocationFromMap(map: Map<String, Any?>): Location {
            return Location(lat =  map["lat"] as Double,
                lng = map["lng"] as Double,
                zoom = (map["zoom"] as Long).toFloat())
        }
    }
}

@Parcelize
data class Location(
    var lat: Double = 0.0,
    var lng: Double = 0.0,
    var zoom: Float = 0f
) : Parcelable