package ie.wit.myapplication.ui.add

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresPermission
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import ie.wit.myapplication.R
import ie.wit.myapplication.databinding.FragmentAddBinding
import ie.wit.myapplication.firebase.FirebaseImageManager
import ie.wit.myapplication.models.FreecycleModel
import ie.wit.myapplication.models.Location
import ie.wit.myapplication.ui.auth.LoggedInViewModel
import ie.wit.myapplication.utils.readImageUri
import ie.wit.myapplication.utils.showImagePicker
import timber.log.Timber
import java.time.LocalDate
import java.util.UUID

class AddFragment : Fragment() {

    private var _binding: FragmentAddBinding? = null
    private lateinit var imageIntentLauncher: ActivityResultLauncher<Intent>
    private val binding get() = _binding!!
    var listing = FreecycleModel()
    private lateinit var addViewModel: AddViewModel
    private val loggedInViewModel: LoggedInViewModel by activityViewModels()

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val REQUEST_LOCATION_PERMISSION = 1
    private lateinit var imageId: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this.requireContext())
    }

    @SuppressLint("MissingPermission")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        Timber.i("ON CREATE VIEW ADD FRAGMENT")
        imageId = UUID.randomUUID().toString()
        _binding = FragmentAddBinding.inflate(inflater, container, false)
        val root: View = binding.root
        enableMyLocation()

        fusedLocationClient.lastLocation.addOnSuccessListener { location: android.location.Location? ->
            if (location != null) {
                listing.location = Location(location.latitude, location.longitude, 15f)
            }
        }
        binding.chooseImage.setOnClickListener {
            showImagePicker(imageIntentLauncher)
        }
        registerImagePickerCallback()

        addViewModel = ViewModelProvider(this).get(AddViewModel::class.java)
        addViewModel.observableStatus.observe(viewLifecycleOwner, Observer { status ->
            status?.let { render(status) }
        })
        setButtonListener(binding)
        return root
    }

    private fun isPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(), Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    @SuppressLint("MissingPermission")
    private fun enableMyLocation() {
        if (!isPermissionGranted()) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf<String>(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_LOCATION_PERMISSION
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.contains(PackageManager.PERMISSION_GRANTED)) {
                enableMyLocation()

            }
        }
    }

    private fun render(status: Boolean) {
        when (status) {
            true -> {
                view?.let {
                    //Uncomment this if you want to immediately return to List
                    //findNavController().popBackStack()
                }
            }
            false -> Toast.makeText(context, getString(R.string.listingError), Toast.LENGTH_LONG)
                .show()
        }
    }

    fun setButtonListener(layout: FragmentAddBinding) {
        layout.btnAdd.setOnClickListener {
            listing.name = binding.name.text.toString()
            listing.contactNumber = binding.phoneNumber.text.toString()
            listing.listingTitle = binding.listingTitle.text.toString()
            listing.listingDescription = binding.listingDescription.text.toString()
            listing.itemAvailable = binding.toggleButton.isChecked
            val dateSelected = LocalDate.of(
                binding.datePicker.year, binding.datePicker.month + 1, binding.datePicker.dayOfMonth
            )
            listing.dateAvailable = dateSelected
            FirebaseImageManager.updateDefaultImage(
                loggedInViewModel.liveFirebaseUser.value?.uid!!,
                R.drawable.logo_image,
                binding.ListingImage
            )

            if (listing.listingTitle.isNotEmpty() && listing.listingDescription.isNotEmpty() && listing.name.isNotEmpty()) {

                Timber.i("FirebaseUser : ${loggedInViewModel.liveFirebaseUser}")

                var newListing = FreecycleModel(
                    name = listing.name,
                    contactNumber = listing.contactNumber,
                    listingTitle = listing.listingTitle,
                    listingDescription = listing.listingDescription,
                    // image = imageId,
                    image = listing.image,
                    itemAvailable = listing.itemAvailable,
                    dateAvailable = listing.dateAvailable,
                    email = loggedInViewModel.liveFirebaseUser.value?.email!!,
                    profilePic = listing.profilePic,

                    )

                if (listing.location?.zoom != 0f) {
                    newListing.location = Location(
                        listing.location?.lat!!,
                        listing.location?.lng!!,
                        listing.location?.zoom!!
                    )
                }
                addViewModel.addListing(
                    loggedInViewModel.liveFirebaseUser, newListing
                )
                Timber.i("ADDING LISTING %s", listing)
                findNavController().navigateUp()
            } else {
                Snackbar.make(it, R.string.all_fields_required, Snackbar.LENGTH_LONG).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()

    }

    private fun registerImagePickerCallback() {
        imageIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                when (result.resultCode) {
                    AppCompatActivity.RESULT_OK -> {
                        if (result.data != null) {
                            Timber.i("Got Result ${result.data!!.data}")
                            listing.image = result.data!!.data.toString()
                            // upload image to firebase
                            FirebaseImageManager.updateListingImage(
                                imageId,
                                readImageUri(result.resultCode, result.data),
                                binding?.ListingImage!!,
                                true
                            )
                            Picasso.get().load(listing.image).into(binding.ListingImage)
                            binding.chooseImage.setText(R.string.edit_image)
                        } // end of if
                    }
                    AppCompatActivity.RESULT_CANCELED -> {}
                    else -> {}
                }
            }
    }

}