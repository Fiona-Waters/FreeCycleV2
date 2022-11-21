package ie.wit.myapplication.ui.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ie.wit.myapplication.models.FreecycleManager
import ie.wit.myapplication.models.FreecycleModel

class ListViewModel : ViewModel() {

    private val listings = MutableLiveData<List<FreecycleModel>>()
    val observableListings: LiveData<List<FreecycleModel>>
        get() = listings

   init {
       load()
   }

    fun load() {
        listings.value = FreecycleManager.findAll()
    }
}