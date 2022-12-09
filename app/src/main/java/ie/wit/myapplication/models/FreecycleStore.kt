package ie.wit.myapplication.models

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser

//interface FreecycleStore {
//    fun findAll(): List<FreecycleModel>
//    fun findAll(email: String, listings : MutableLiveData<List<FreecycleModel>>)
//    fun create(listing: FreecycleModel)
//    fun update(listing: FreecycleModel)
//    fun delete(listing: FreecycleModel)
//    fun delete(email: String, id: String)
//}

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

    fun create(firebaseUser: MutableLiveData<FirebaseUser>, listing: FreecycleModel)
    fun delete(userid: String, listingid: String)
    fun update(userid: String, listingid: String, listing: FreecycleModel)


}