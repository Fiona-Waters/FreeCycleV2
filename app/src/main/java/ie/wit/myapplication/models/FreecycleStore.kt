package ie.wit.myapplication.models

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser

interface FreecycleStore {
    fun findAll(listings: MutableLiveData<List<FreecycleModel>>)
    fun findAll(
        userid: String,
        listings: MutableLiveData<List<FreecycleModel>>
    )

    fun findById(
        userid: String, listingid: String,
        listing: MutableLiveData<FreecycleModel>
    )

    fun findById(
        listingid: String,
        listing: MutableLiveData<FreecycleModel>
    )

    fun create(firebaseUser: MutableLiveData<FirebaseUser>, listing: FreecycleModel)
    fun delete(userid: String, listingid: String)
    fun update(userid: String, listingid: String, listing: FreecycleModel)


}