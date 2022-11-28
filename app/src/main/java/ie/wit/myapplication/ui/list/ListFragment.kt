package ie.wit.myapplication.ui.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ie.wit.myapplication.adapters.FreecycleAdapter
import ie.wit.myapplication.adapters.FreecycleListener
import ie.wit.myapplication.databinding.FragmentListBinding
import ie.wit.myapplication.main.MainApp
import ie.wit.myapplication.models.FreecycleModel
import ie.wit.myapplication.ui.auth.LoggedInViewModel
import ie.wit.myapplication.utils.SwipeToDeleteCallback
import ie.wit.myapplication.utils.SwipeToEditCallback
import timber.log.Timber

class ListFragment : Fragment(), FreecycleListener {

    private var _binding: FragmentListBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    lateinit var app: MainApp
    private val listViewModel: ListViewModel by activityViewModels()
    private val loggedInViewModel : LoggedInViewModel by activityViewModels()

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
        val root = binding.root
        binding.recyclerView.layoutManager = LinearLayoutManager(activity)
     //   listViewModel = ViewModelProvider(this).get(ListViewModel::class.java)
        listViewModel.observableListings.observe(viewLifecycleOwner, Observer { listings ->
            listings?.let { render(listings as ArrayList<FreecycleModel>)
                checkSwipeRefresh()
            }
        })

        setSwipeRefresh()

        val swipeDeleteHandler = object : SwipeToDeleteCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            //    showLoader(loader,"Deleting Donation")
                val adapter = binding.recyclerView.adapter as FreecycleAdapter
                adapter.removeAt(viewHolder.adapterPosition)
                listViewModel.delete(listViewModel.liveFirebaseUser.value?.uid!!,
                    (viewHolder.itemView.tag as FreecycleModel).uid!!)

             //   hideLoader(loader)
            }
        }
        val itemTouchDeleteHelper = ItemTouchHelper(swipeDeleteHandler)
        itemTouchDeleteHelper.attachToRecyclerView(binding.recyclerView)

        val swipeEditHandler = object : SwipeToEditCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                onListingClick(viewHolder.itemView.tag as FreecycleModel)
            }
        }
        val itemTouchEditHelper = ItemTouchHelper(swipeEditHandler)
        itemTouchEditHelper.attachToRecyclerView(binding.recyclerView)

        return root
    }

    private fun render(listings: ArrayList<FreecycleModel>) {
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
//        val action = ListFragmentDirections.actionListFragmentToViewFragment(listing.uid!!)
//        findNavController().navigate(action)
    }

    private fun setSwipeRefresh() {
        binding.swiperefresh.setOnRefreshListener {
            binding.swiperefresh.isRefreshing = true
          //  showLoader(loader,"Downloading Donations")
            listViewModel.load()
        }
    }

    private fun checkSwipeRefresh() {
        if (binding.swiperefresh.isRefreshing)
            binding.swiperefresh.isRefreshing = false
    }

    override fun onResume() {
        super.onResume()
        loggedInViewModel.liveFirebaseUser.observe(viewLifecycleOwner, Observer { firebaseUser ->
            if (firebaseUser != null) {
                listViewModel.liveFirebaseUser.value = firebaseUser
                listViewModel.load()
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}