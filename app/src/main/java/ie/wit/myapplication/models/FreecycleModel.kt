package ie.wit.myapplication.models

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.LocalDate

@Parcelize
data class FreecycleModel(
    var id: Long = 0,
    var userId: Long = 0,
    var name: String = "",
    var contactNumber: String = "",
    var listingTitle: String = "",
    var listingDescription: String = "",
    var image: Uri = Uri.EMPTY,
    var lat: Double = 0.0,
    var lng: Double = 0.0,
    var zoom: Float = 0f,
    var itemAvailable: Boolean = true,
    var dateAvailable: LocalDate = LocalDate.now()
) : Parcelable


@Parcelize
data class Location(
    var lat: Double = 0.0,
    var lng: Double = 0.0,
    var zoom: Float = 0f
) : Parcelable