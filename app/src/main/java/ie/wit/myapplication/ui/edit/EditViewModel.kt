package ie.wit.myapplication.ui.edit

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ie.wit.myapplication.firebase.FirebaseDBManager
import ie.wit.myapplication.models.FreecycleModel
import ie.wit.myapplication.utils.readImageFromPath
import timber.log.Timber
import java.io.ByteArrayOutputStream
import java.io.File
import android.content.Context



class EditViewModel(val context: Context)  : ViewModel() {
    private val listing = MutableLiveData<FreecycleModel>()

    var observableListing: LiveData<FreecycleModel>
        get() = listing
        set(value) {listing.value = value.value}

    fun getListing(userid: String, id: String) {
        try {
            FirebaseDBManager.findById(userid, id, listing)
            Timber.i("ViewListing getListing() success : ${listing.value.toString()}")
        } catch (e: Exception) {
            Timber.i("ViewListing getListing() error : ${e.message}")
        }
    }

    fun updateListing(userid: String, id: String, listing: FreecycleModel) {
        try {
            FirebaseDBManager.update(userid, id, listing)
            Timber.i("ViewListing update() success : $listing")
        } catch (e: Exception) {
            Timber.i("ViewListing update() error : ${e.message}")
        }
    }
}