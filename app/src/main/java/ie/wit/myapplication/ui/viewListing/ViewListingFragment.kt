package ie.wit.myapplication.ui.viewListing

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import ie.wit.myapplication.databinding.FragmentViewListingBinding
import ie.wit.myapplication.ui.auth.LoggedInViewModel

class ViewListingFragment : Fragment() {

    private lateinit var viewListingViewModel: ViewListingViewModel

    private val args by navArgs<ViewListingFragmentArgs>()
    private var _binding: FragmentViewListingBinding? = null
    private val binding get() = _binding!!
    private val loggedInViewModel: LoggedInViewModel by activityViewModels()
    //   private val listViewModel: ListViewModel by activityViewModels()

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        Timber.i("ON CREATE VIEW LISTING FRAGMENT")
//    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentViewListingBinding.inflate(inflater, container, false)
        val root = binding.root

        viewListingViewModel = ViewModelProvider(this).get(ViewListingViewModel::class.java)
        viewListingViewModel.observableListing.observe(viewLifecycleOwner, Observer { render() })

        return root
    }

    private fun render() {
        binding.listingvm = viewListingViewModel
    }

    override fun onResume() {
        super.onResume()
        viewListingViewModel.getListing(
            loggedInViewModel.liveFirebaseUser.value?.uid!!,
            args.listingid
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}

