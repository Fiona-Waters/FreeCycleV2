package ie.wit.myapplication.ui.add

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import ie.wit.myapplication.firebase.FirebaseDBManager
import ie.wit.myapplication.models.FreecycleModel

class AddViewModel : ViewModel() {

    private val status = MutableLiveData<Boolean>()

    val observableStatus: LiveData<Boolean>
        get() = status

    fun addListing(firebaseUser: MutableLiveData<FirebaseUser>, listing: FreecycleModel) {
        status.value = try {
            FirebaseDBManager.create(firebaseUser, listing)
            true
        } catch (e: IllegalArgumentException) {
            false
        }
    }
}