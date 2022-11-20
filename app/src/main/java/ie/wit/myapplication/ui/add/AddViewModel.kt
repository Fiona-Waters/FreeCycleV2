package ie.wit.myapplication.ui.add

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ie.wit.myapplication.models.FreecycleManager
import ie.wit.myapplication.models.FreecycleModel
import timber.log.Timber

class AddViewModel : ViewModel() {

//    private val _text = MutableLiveData<String>().apply {
//        value = "This is gallery Fragment"
//    }
//    val text: LiveData<String> = _text

    private val status = MutableLiveData<Boolean>()

    val observableStatus: LiveData<Boolean>
        get() = status

    fun addListing(listing: FreecycleModel) {
        status.value = try {
            FreecycleManager.create(listing)
            //Timber.i("LOG true :%v", listing)
            true
        } catch (e: IllegalArgumentException) {
            Timber.i("LOG false :%v", e)
            false
        }
    }
}