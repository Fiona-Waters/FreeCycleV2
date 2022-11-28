package ie.wit.myapplication.models
//
//import androidx.lifecycle.MutableLiveData
//import com.google.firebase.auth.FirebaseUser
//import timber.log.Timber
//
//var lastId = 0L
//
//internal fun getId(): String {
//    //TODO
//    return ""
//}
//
//object FreecycleManager : FreecycleStore {
//
//    private val listings = MutableLiveData<List<FreecycleModel>>()
//
//    override fun findAll(listings: MutableLiveData<List<FreecycleModel>>) {
//        //return listings
//    }
//
//    override fun findAll(email: String, listings: MutableLiveData<List<FreecycleModel>>) {
//        TODO("Not yet implemented")
//    }
//
//    override fun findById(
//        userid: String,
//        listingid: String,
//        listing: MutableLiveData<FreecycleModel>
//    ) {
//        TODO("Not yet implemented")
//    }
//
//    override fun create(firebaseUser: MutableLiveData<FirebaseUser>, listing: FreecycleModel) {
//        listing.uid = getId()
//      //  listings.add(listing)
//    //    logAll()
//    }
//
////    override fun update(listing: FreecycleModel) {
////        var foundListing: FreecycleModel? = listings.find { l -> l.uid == listing.uid }
////        if (foundListing != null) {
////            foundListing.name = listing.name
////            foundListing.contactNumber = listing.contactNumber
////            foundListing.listingTitle = listing.listingTitle
////            foundListing.listingDescription = listing.listingDescription
////            foundListing.image = listing.image
////            foundListing.itemAvailable = listing.itemAvailable
////            foundListing.dateAvailable = listing.dateAvailable
////            foundListing.lat = listing.lat
////            foundListing.lng = listing.lng
////            foundListing.zoom = listing.zoom
////            logAll()
////        }
////    }
//
////    override fun delete(listing: FreecycleModel) {
////        listings.remove(listing)
////        logAll()
////    }
//
//    override fun delete(email: String, id: String) {
//        TODO("Not yet implemented")
//    }
//
//    override fun update(userid: String, listingid: String, listing: FreecycleModel) {
//        TODO("Not yet implemented")
//    }
////
////    fun logAll() {
////        listings.forEach { Timber.i("${it}") }
////    }
//
//
//}