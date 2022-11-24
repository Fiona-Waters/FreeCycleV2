package ie.wit.myapplication.ui.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import ie.wit.myapplication.adapters.FreecycleAdapter
import ie.wit.myapplication.adapters.FreecycleListener
import ie.wit.myapplication.databinding.FragmentListBinding
import ie.wit.myapplication.main.MainApp
import ie.wit.myapplication.models.FreecycleModel
import timber.log.Timber

class ListFragment : Fragment(), FreecycleListener {

    private var _binding: FragmentListBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    lateinit var app: MainApp
    private lateinit var listViewModel: ListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.i("ON CREATE LIST FRAGMENT")

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListBinding.inflate(inflater, container, false)
        binding.recyclerView.layoutManager = LinearLayoutManager(activity)
        listViewModel = ViewModelProvider(this).get(ListViewModel::class.java)
        listViewModel.observableListings.observe(viewLifecycleOwner, Observer { listings ->
            listings?.let { render(listings) }
        })
        return binding.root
    }

    private fun render(listings: List<FreecycleModel>) {
        binding.recyclerView.adapter = FreecycleAdapter(listings, this)
        if (listings.isEmpty()) {
            binding.recyclerView.visibility = View.GONE
            //    binding.donationsNotFound.visibility = View.VISIBLE
        } else {
            binding.recyclerView.visibility = View.VISIBLE
            //   binding.donationsNotFound.visibility = View.GONE
        }
    }

    override fun onListingClick(listing: FreecycleModel) {
        //TODO when listing is clicked go to view listing fragment! have edit and delete option.
//        val action = ListFragmentDirections.actionListFragmentToViewFragment(listing.id)
//        findNavController().navigate(action)
    }

    override fun onResume() {
        super.onResume()
        listViewModel.load()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}