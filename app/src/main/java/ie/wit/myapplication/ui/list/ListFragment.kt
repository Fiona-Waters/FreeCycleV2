package ie.wit.myapplication.ui.list

import android.os.Bundle
import android.view.*
import android.widget.ImageView
import androidx.appcompat.widget.SwitchCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import ie.wit.myapplication.R
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
    private val binding get() = _binding!!

    private val listViewModel: ListViewModel by activityViewModels()
    private val loggedInViewModel: LoggedInViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.i("ON CREATE LIST FRAGMENT")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListBinding.inflate(inflater, container, false)
        val root = binding.root
        setupMenu()
        binding.recyclerView.layoutManager = LinearLayoutManager(activity)
        listViewModel.observableListings.observe(viewLifecycleOwner, Observer { listings ->
            listings?.let {
                render(listings as ArrayList<FreecycleModel>)
                checkSwipeRefresh()
            }
        })

        setSwipeRefresh()

        val swipeDeleteHandler = object : SwipeToDeleteCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = binding.recyclerView.adapter as FreecycleAdapter
                adapter.removeAt(viewHolder.adapterPosition)
                listViewModel.delete(
                    listViewModel.liveFirebaseUser.value?.uid!!,
                    (viewHolder.itemView.tag as FreecycleModel).uid!!
                )
            }
        }
        val itemTouchDeleteHelper = ItemTouchHelper(swipeDeleteHandler)
        itemTouchDeleteHelper.attachToRecyclerView(binding.recyclerView)


        val swipeEditHandler = object : SwipeToEditCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                onListingSwipeRight(viewHolder.itemView.tag as FreecycleModel)
            }
        }
        val itemTouchEditHelper = ItemTouchHelper(swipeEditHandler)
        itemTouchEditHelper.attachToRecyclerView(binding.recyclerView)

        return root
    }

    private fun setupMenu() {
        (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {
            override fun onPrepareMenu(menu: Menu) {
            }

            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_list, menu)

                val item = menu.findItem(R.id.toggleListings) as MenuItem
                item.setActionView(R.layout.togglebutton_layout)
                val toggleListings: SwitchCompat = item.actionView!!.findViewById(R.id.toggleButton)
                toggleListings.isChecked = false

                toggleListings.setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked) listViewModel.loadAll()
                    else listViewModel.load()

                }
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return NavigationUI.onNavDestinationSelected(
                    menuItem, requireView().findNavController()
                )
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun render(listings: ArrayList<FreecycleModel>) {
        binding.recyclerView.adapter =
            FreecycleAdapter(listings, this, listViewModel.readOnly.value!!)
        if (listings.isEmpty()) {
            binding.recyclerView.visibility = View.GONE
        } else {
            binding.recyclerView.visibility = View.VISIBLE
        }
    }

    override fun onListingClick(listing: FreecycleModel) {
        val action = ListFragmentDirections.actionListFragmentToViewListingFragment(listing.uid!!)
        findNavController().navigate(action)
    }

    fun onListingSwipeRight(listing: FreecycleModel) {
        val action = ListFragmentDirections.actionListFragmentToEditFragment(listing.uid!!)
        findNavController().navigate(action)
    }

    private fun setSwipeRefresh() {
        binding.swiperefresh.setOnRefreshListener {
            binding.swiperefresh.isRefreshing = true
            if (listViewModel.readOnly.value!!) listViewModel.loadAll()
            else listViewModel.load()
        }
    }

    private fun checkSwipeRefresh() {
        if (binding.swiperefresh.isRefreshing) binding.swiperefresh.isRefreshing = false
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