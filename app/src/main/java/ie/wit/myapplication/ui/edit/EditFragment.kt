package ie.wit.myapplication.ui.edit

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import ie.wit.myapplication.databinding.FragmentEditBinding
import ie.wit.myapplication.ui.auth.LoggedInViewModel

class EditFragment : Fragment() {

    private lateinit var editViewModel: EditViewModel
    private val args by navArgs<EditFragmentArgs>()
    private var _binding: FragmentEditBinding? = null
    private val binding get() = _binding!!
    private val loggedInViewModel: LoggedInViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditBinding.inflate(inflater, container, false)
        val root = binding.root

        editViewModel = ViewModelProvider(this)[EditViewModel::class.java]
        editViewModel.observableListing.observe(viewLifecycleOwner) { render() }

        binding.btnUpdate.setOnClickListener {
            val updatedListing = binding.listingvm?.observableListing!!.value!!
            //TODO handle datepicker and image update etc

            editViewModel.updateListing(
                loggedInViewModel.liveFirebaseUser.value?.uid!!,
                args.listingid,
               updatedListing
            )
            findNavController().navigateUp()
        }
        // TODO add update location functionality
        // TODO add edit image functionality

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
        binding.listingvm = editViewModel
    }

    override fun onResume() {
        super.onResume()
        editViewModel.getListing(
            loggedInViewModel.liveFirebaseUser.value?.uid!!,
            args.listingid
        )
    }

}