package ie.wit.myapplication.ui.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ie.wit.myapplication.models.Location

class MapViewModel : ViewModel() {
    private val location = MutableLiveData<Location>()

    var observableLocation: LiveData<Location>
        get() = location
        set(value) {
            location.value = value.value
        }
}