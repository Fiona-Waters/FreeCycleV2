package ie.wit.myapplication.ui.map

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil.setContentView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import ie.wit.myapplication.R
import ie.wit.myapplication.databinding.FragmentMapBinding
import ie.wit.myapplication.models.Location
import ie.wit.myapplication.ui.list.ListViewModel

class MapFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMarkerDragListener,
    GoogleMap.OnMarkerClickListener {

    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!

    private lateinit var mapViewModel: MapViewModel
    private lateinit var listViewModel: ListViewModel
    private lateinit var map: GoogleMap
    private var mapReady = false
    var location = Location()
    private val args by navArgs<MapFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMapBinding.inflate(inflater, container, false)
        val root = binding.root
        mapViewModel = ViewModelProvider(this).get(MapViewModel::class.java)
        mapViewModel.observableLocation.observe(viewLifecycleOwner, Observer { })
        location = args.location
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync { googleMap ->
         //   addMarkers(googleMap)
            map = googleMap
            mapReady = true
            onMapReady(map)
        }
        return root
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        val loc = LatLng(args.location.lat, args.location.lng)
        val options = MarkerOptions()
            .title("Listing")
            .snippet("GPS : $loc")
            .draggable(true)
            .position(loc)
        map.addMarker(options)
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, args.location.zoom))
        map.setOnMarkerDragListener(this)
        map.setOnMarkerClickListener(this)
    }

    override fun onMarkerDrag(marker: Marker) {
    }

    override fun onMarkerDragEnd(marker: Marker) {
        location.lat = marker.position.latitude
        location.lng = marker.position.longitude
        location.zoom = map.cameraPosition.zoom
        //TODO Call firebase listing update
    }

    override fun onMarkerDragStart(marker: Marker) {
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        val loc = LatLng(location.lat, location.lng)
        marker.snippet = "GPS : $loc"
        return false
    }


//    private fun addMarkers(googleMap: GoogleMap) {
//        val loc = LatLng(52.245696, -7.139102)
//        googleMap.addMarker(MarkerOptions().title("You are here").position(loc))
//    }

//    override fun onBackPressed() {
//        val resultIntent = Intent()
//        resultIntent.putExtra("location", location)
//        setResult(Activity.RESULT_OK, resultIntent)
//        finish()
//        super.onBackPressed()
//    }
}