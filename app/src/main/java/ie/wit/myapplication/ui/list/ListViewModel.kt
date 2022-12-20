package ie.wit.myapplication.ui.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import ie.wit.myapplication.firebase.FirebaseDBManager
import ie.wit.myapplication.models.FreecycleModel
import timber.log.Timber

class ListViewModel : ViewModel() {

    private val listings = MutableLiveData<List<FreecycleModel>>()
    val observableListings: LiveData<List<FreecycleModel>>
        get() = listings
    val liveFirebaseUser = MutableLiveData<FirebaseUser>()
    var readOnly = MutableLiveData(false)

    init {
        load()
    }

    fun load() {
        try {
            readOnly.value = false
            FirebaseDBManager.findAll(liveFirebaseUser.value?.uid!!, listings)
            Timber.i("List Load Success : ${listings.value.toString()}")
        } catch (e: Exception) {
            Timber.i("List Load Error : ${e.message}")
        }
    }

    fun loadAll() {
        try {
            readOnly.value = true
            FirebaseDBManager.findAll(listings)
            Timber.i("Report LoadAll Success : ${listings.value.toString()}")
        } catch (e: Exception) {
            Timber.i("Report LoadAll Error : ${e.message}")
        }
    }

    fun delete(email: String, id: String) {
        try {
            FirebaseDBManager.delete(email, id)
            Timber.i("Listing delete success")
        } catch (e: Exception) {
            Timber.i("Delete Error : ${e.message}")
        }
    }
}