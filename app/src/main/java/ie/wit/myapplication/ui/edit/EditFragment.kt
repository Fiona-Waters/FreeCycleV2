package ie.wit.myapplication.ui.edit

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.squareup.picasso.Picasso
import ie.wit.myapplication.R
import ie.wit.myapplication.databinding.FragmentEditBinding
import ie.wit.myapplication.models.FreecycleModel
import ie.wit.myapplication.models.Location
import ie.wit.myapplication.ui.add.AddFragmentDirections
import ie.wit.myapplication.ui.auth.LoggedInViewModel
import ie.wit.myapplication.utils.showImagePicker
import timber.log.Timber

class EditFragment : Fragment() {

    private lateinit var editViewModel: EditViewModel
    private val args by navArgs<EditFragmentArgs>()
    private var _binding: FragmentEditBinding? = null
    private val binding get() = _binding!!
    private lateinit var imageIntentLauncher: ActivityResultLauncher<Intent>
    private val loggedInViewModel: LoggedInViewModel by activityViewModels()
    val listing = FreecycleModel()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditBinding.inflate(inflater, container, false)
        val root = binding.root

        editViewModel = ViewModelProvider(this).get(EditViewModel::class.java)
        editViewModel.observableListing.observe(viewLifecycleOwner, Observer { render() })

        binding.chooseImage.setOnClickListener{
            showImagePicker(imageIntentLauncher)
        }
        registerImagePickerCallback()

        binding.pickupLocation.setOnClickListener {
            val location = Location(52.245696, -7.139102, 15f)
            if (listing.location?.zoom!! != 0f) {
                location.lat = listing.location?.lat!!
                location.lng = listing.location?.lng!!
                location.zoom = listing.location?.zoom!!
            }
            val action = EditFragmentDirections.actionEditFragmentToMapFragment(location)
            findNavController().navigate(action)
        }



        binding.btnUpdate.setOnClickListener {
            val updatedListing = binding.listingvm?.observableListing!!.value!!

            //TODO handle datepicker and image update etc

            editViewModel.updateListing(
                loggedInViewModel.liveFirebaseUser.value?.uid!!,
                args.listingid,
               updatedListing,
            )
            findNavController().navigateUp()
        }
        // TODO add update location functionality
        // TODO add edit image functionality

        return root
    }

    fun setButtonListener(layout: FragmentEditBinding) {
        layout.btnUpdate.setOnClickListener{

            listing.location
            listing.image
        }
    }

    private fun render() {
        binding.datePicker.init(
            editViewModel.observableListing.value?.dateAvailable!!.year,
            editViewModel.observableListing.value?.dateAvailable!!.month.value,
            editViewModel.observableListing.value?.dateAvailable!!.dayOfMonth,
        ) { _, year, month, day ->
            val month = month + 1
            val msg = "You Selected: $day/$month/$year"
        }

        binding.toggleButton.isChecked =
            editViewModel.observableListing.value?.itemAvailable == true
        // TODO next line - location and image

        val image = view?.findViewById<ImageView>(R.id.imageIcon)
        if (editViewModel.observableListing.value?.image != "") {
            image?.background = null
        }
        Picasso.get().load(editViewModel.observableListing.value?.image).into(binding.ListingImage)

        if (listing.location?.zoom != 0f) {
            editViewModel.observableListing.value?.location?.lat = listing.location?.lat!!
            editViewModel.observableListing.value?.location?.lng = listing.location?.lng!!
            editViewModel.observableListing.value?.location?.zoom = listing.location?.zoom!!
        }
        binding.listingvm = editViewModel
    }

    override fun onResume() {
        super.onResume()
        editViewModel.getListing(
            loggedInViewModel.liveFirebaseUser.value?.uid!!,
            args.listingid
        )
    }

    private fun registerImagePickerCallback() {
        imageIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                when (result.resultCode) {
                    AppCompatActivity.RESULT_OK -> {
                        if (result.data != null) {
                            Timber.i("Got Result ${result.data!!.data}")
                            editViewModel.observableListing.value?.image = result.data!!.data.toString()!!
                            Picasso.get().load(editViewModel.observableListing.value?.image).into(binding.ListingImage)
                            binding.chooseImage.setText(R.string.edit_image)
                        } // end of if
                    }
                    AppCompatActivity.RESULT_CANCELED -> {}
                    else -> {}
                }
            }
    }

}