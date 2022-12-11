package ie.wit.myapplication.ui.add

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.navigateUp
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import ie.wit.myapplication.R
import ie.wit.myapplication.databinding.FragmentAddBinding
import ie.wit.myapplication.main.MainApp
import ie.wit.myapplication.models.FreecycleModel
import ie.wit.myapplication.models.Location
import ie.wit.myapplication.ui.auth.LoggedInViewModel
import ie.wit.myapplication.ui.list.ListViewModel
import ie.wit.myapplication.utils.showImagePicker
import timber.log.Timber
import java.time.LocalDate

class AddFragment : Fragment() {

    private var _binding: FragmentAddBinding? = null
    private lateinit var imageIntentLauncher: ActivityResultLauncher<Intent>

    // TODO map intent launcher
    // lateinit var app: MainApp
    private val binding get() = _binding!!
    var listing = FreecycleModel()
    private lateinit var addViewModel: AddViewModel
    private val listViewModel: ListViewModel by activityViewModels()
    private val loggedInViewModel: LoggedInViewModel by activityViewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Timber.i("ON CREATE VIEW ADD FRAGMENT")

        _binding = FragmentAddBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.pickupLocation.setOnClickListener{
            val location = Location(52.24, -7.13, 15f)
//            if (listing.zoom != 0f) {
//                location.lat = listing.lat
//                location.lng = listing.lng
//                location.zoom = listing.zoom
//            }
            val action = AddFragmentDirections.actionAddFragmentToMapFragment()
            findNavController().navigate(action)
//            val launcherIntent =
//                Intent(this, MapActivity::class.java).putExtra("location", location)
//            mapIntentLauncher.launch(launcherIntent)
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

    private fun render(status: Boolean) {
        when (status) {
            true -> {
                view?.let {
                    //Uncomment this if you want to immediately return to Report
                    //findNavController().popBackStack()
                }
            }
            false -> Toast.makeText(context, getString(R.string.listingError), Toast.LENGTH_LONG)
                .show()
        }
    }

    fun setButtonListener(layout: FragmentAddBinding) {
        layout.btnAdd.setOnClickListener {
            //      listing.userId = app.user?.userId ?: 0
            listing.name = binding.name.text.toString()
            listing.contactNumber = binding.phoneNumber.text.toString()
            listing.listingTitle = binding.listingTitle.text.toString()
            listing.listingDescription = binding.listingDescription.text.toString()
            listing.itemAvailable = binding.toggleButton.isChecked
            val dateSelected = LocalDate.of(
                binding.datePicker.year, binding.datePicker.month + 1, binding.datePicker.dayOfMonth
            )
            listing.dateAvailable = dateSelected

            if (listing.listingTitle.isNotEmpty() && listing.listingDescription.isNotEmpty() && listing.name.isNotEmpty()) {
//
                Timber.i("FirebaseUser : ${loggedInViewModel.liveFirebaseUser}")
                addViewModel.addListing(
                    loggedInViewModel.liveFirebaseUser,
                    FreecycleModel(
                        name = listing.name,
                        contactNumber = listing.contactNumber,
                        listingTitle = listing.listingTitle,
                        listingDescription = listing.listingDescription,
             //           image = listing.image,
                        itemAvailable = listing.itemAvailable,
                        dateAvailable = listing.dateAvailable,
                        email = loggedInViewModel.liveFirebaseUser.value?.email!!
                    )
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

    // TODO override fun onResume() {

    private fun registerImagePickerCallback() {
        imageIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                when (result.resultCode) {
                    AppCompatActivity.RESULT_OK -> {
                        if (result.data != null) {
                            Timber.i("Got Result ${result.data!!.data}")
                            //         listing.image = result.data!!.data!!
                            //          Picasso.get().load(listing.image).into(binding.ListingImage)
                            binding.chooseImage.setText(R.string.edit_image)
                        } // end of if
                    }
                    AppCompatActivity.RESULT_CANCELED -> {}
                    else -> {}
                }
            }
    }
}