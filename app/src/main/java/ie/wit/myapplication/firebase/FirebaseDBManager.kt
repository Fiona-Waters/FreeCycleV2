package ie.wit.myapplication.firebase

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import ie.wit.myapplication.models.FreecycleModel
import ie.wit.myapplication.models.FreecycleStore
import timber.log.Timber

object FirebaseDBManager : FreecycleStore {

    var database: DatabaseReference = FirebaseDatabase.getInstance().reference

    override fun findAll(listings: MutableLiveData<List<FreecycleModel>>) {
        database.child("listings")
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    Timber.i("Firebase Listing error : ${error.message}")
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    val localList = ArrayList<FreecycleModel>()
                    val children = snapshot.children
                    children.forEach {
                  //      val listing = it.getValue(FreecycleModel::class.java)
                        val data = it.value as HashMap<String, Any?>
                        val listing = FreecycleModel.fromMap(data!!)
                        localList.add(listing!!)
                    }
                    database.child("listings")
                        .removeEventListener(this)

                    listings.value = localList
                }
            })    }

    override fun findAll(userid: String, listings: MutableLiveData<List<FreecycleModel>>) {
        database.child("user-listings").child(userid)
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    Timber.i("Firebase Listing error : ${error.message}")
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    val localList = ArrayList<FreecycleModel>()
                    val children = snapshot.children
                    children.forEach {
                        //val listing = it.getValue(FreecycleModel::class.java)
                        val data = it.value as HashMap<String, Any?>
                        val listing = FreecycleModel.fromMap(data!!)
                        localList.add(listing)
                    }
                    database.child("user-listings").child(userid)
                        .removeEventListener(this)

                    listings.value = localList
                }
            })
    }

    override fun findById(
        userid: String,
        listingid: String,
        listing: MutableLiveData<FreecycleModel>
    ) {
        database.child("user-listings").child(userid)
            .child(listingid).get().addOnSuccessListener {

                // listing.value = it.getValue(FreecycleModel::class.java)
                val data = it.value as HashMap<String, Any?>
                val l = FreecycleModel.fromMap(data!!)

                listing.value = l

                Timber.i("firebase Got value ${it.value}")
            }.addOnFailureListener {
                Timber.e("firebase Error getting data $it")
            }
    }

    override fun findById(
        listingid: String,
        listing: MutableLiveData<FreecycleModel>
    ) {
        database.child("listings").child(listingid)
            .get().addOnSuccessListener {

                // listing.value = it.getValue(FreecycleModel::class.java)
                val data = it.value as HashMap<String, Any?>
                val l = FreecycleModel.fromMap(data!!)

                listing.value = l

                Timber.i("firebase Got value ${it.value}")
            }.addOnFailureListener {
                Timber.e("firebase Error getting data $it")
            }
    }

    override fun create(firebaseUser: MutableLiveData<FirebaseUser>, listing: FreecycleModel) {
        Timber.i("Firebase DB Reference : $database")

        val uid = firebaseUser.value!!.uid
        val key = database.child("listings").push().key
        if (key == null) {
            Timber.i("Firebase Error : Key Empty")
            return
        }
        listing.uid = key
        val listingValues = listing.toMap()

        val childAdd = HashMap<String, Any>()
        childAdd["/listings/$key"] = listingValues
        childAdd["/user-listings/$uid/$key"] = listingValues

        database.updateChildren(childAdd)
    }

    override fun delete(userid: String, listingid: String) {
        val childDelete: MutableMap<String, Any?> = HashMap()
        childDelete["/listings/$listingid"] = null
        childDelete["/user-listings/$userid/$listingid"] = null

        database.updateChildren(childDelete)
    }

    override fun update(userid: String, listingid: String, listing: FreecycleModel) {
        val listingValues = listing.toMap()

        val childUpdate: MutableMap<String, Any?> = HashMap()
        childUpdate["listings/$listingid"] = listingValues
        childUpdate["user-listings/$userid/$listingid"] = listingValues

        database.updateChildren(childUpdate)
    }

    fun updateImageRef(userid: String,imageUri: String) {

        val userListings = database.child("user-listings").child(userid)
        val allListings = database.child("listings")

        userListings.addListenerForSingleValueEvent(
            object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {}
                override fun onDataChange(snapshot: DataSnapshot) {
                    snapshot.children.forEach {
                        //Update Users imageUri
                        it.ref.child("profilepic").setValue(imageUri)
                        //Update all listings that match 'it'
                        val listing = it.getValue(FreecycleModel::class.java)
                        allListings.child(listing!!.uid!!)
                            .child("profilepic").setValue(imageUri)
                    }
                }
            })
    }

}