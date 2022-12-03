package ie.wit.myapplication.ui.viewListing

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ie.wit.myapplication.firebase.FirebaseDBManager
import ie.wit.myapplication.models.FreecycleModel
import timber.log.Timber

class ViewListingViewModel : ViewModel() {

    private val listing = MutableLiveData<FreecycleModel>()

    var observableListing: LiveData<FreecycleModel>
        get() = listing
        set(value) {
            listing.value = value.value
        }

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