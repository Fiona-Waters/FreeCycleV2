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
import ie.wit.myapplication.firebase.FirebaseImageManager
import ie.wit.myapplication.models.FreecycleModel
import ie.wit.myapplication.models.Location
import ie.wit.myapplication.ui.add.AddFragmentDirections
import ie.wit.myapplication.ui.auth.LoggedInViewModel
import ie.wit.myapplication.utils.showImagePicker
import timber.log.Timber
import java.time.LocalDate

class EditFragment : Fragment() {

    private lateinit var editViewModel: EditViewModel
    private val args by navArgs<EditFragmentArgs>()
    private var _binding: FragmentEditBinding? = null
    private val binding get() = _binding!!
    private lateinit var imageIntentLauncher: ActivityResultLauncher<Intent>
    private val loggedInViewModel: LoggedInViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditBinding.inflate(inflater, container, false)
        val root = binding.root

        editViewModel = ViewModelProvider(requireActivity()).get(EditViewModel::class.java)
        // calling get listing here as have validation in on resume in case of null values
        editViewModel.getListing(
            loggedInViewModel.liveFirebaseUser.value?.uid!!, args.listingid
        )
        editViewModel.observableListing.observe(viewLifecycleOwner, Observer { render() })

        binding.chooseImage.setOnClickListener {
            showImagePicker(imageIntentLauncher)
        }
        registerImagePickerCallback()

        binding.pickupLocation.setOnClickListener {
            val location = Location(52.245696, -7.139102, 15f)
            if (editViewModel.observableListing.value?.location?.zoom != 0f) {
                location.lat = editViewModel.observableListing.value?.location?.lat!!
                location.lng = editViewModel.observableListing.value?.location?.lng!!
                location.zoom = editViewModel.observableListing.value?.location?.zoom!!
            }
            val action =
                EditFragmentDirections.actionEditFragmentToMapFragment(editViewModel.observableListing.value?.location!!)
            findNavController().navigate(action)
        }



        binding.btnUpdate.setOnClickListener {
            val updatedListing = editViewModel.observableListing!!.value!!
            val dateSelected = LocalDate.of(
                binding.datePicker.year, binding.datePicker.month + 1, binding.datePicker.dayOfMonth
            )
            updatedListing.dateAvailable = dateSelected

            editViewModel.updateListing(
                loggedInViewModel.liveFirebaseUser.value?.uid!!,
                args.listingid,
                updatedListing,
            )
            findNavController().navigateUp()
        }
        return root
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

        if (editViewModel.observableListing.value?.image != "") {
            Picasso.get().load(editViewModel.observableListing.value?.image)
                .into(binding.ListingImage)
        }
        binding.listingvm = editViewModel
    }

    override fun onResume() {
        super.onResume()
        if (editViewModel.observableListing.value == null) {
            editViewModel.getListing(
                loggedInViewModel.liveFirebaseUser.value?.uid!!, args.listingid
            )
        }
    }

    private fun registerImagePickerCallback() {
        imageIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                when (result.resultCode) {
                    AppCompatActivity.RESULT_OK -> {
                        if (result.data != null) {
                            Timber.i("Got Result ${result.data!!.data}")
                            var updated = editViewModel.observableListing.value
                            updated?.image = result.data!!.data.toString()!!
                            Picasso.get().load(updated?.image).into(binding.ListingImage)
                            binding.chooseImage.setText(R.string.edit_image)
                            editViewModel.updateListing(
                                loggedInViewModel.liveFirebaseUser.value?.uid!!,
                                args.listingid,
                                updated!!
                            )
                        } // end of if
                    }
                    AppCompatActivity.RESULT_CANCELED -> {}
                    else -> {}
                }
            }
    }


}