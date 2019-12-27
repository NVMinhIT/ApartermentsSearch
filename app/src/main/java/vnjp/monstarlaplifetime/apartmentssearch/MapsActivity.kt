package vnjp.monstarlaplifetime.apartmentssearch

import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.location.Location
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*


class MapsActivity : AppCompatActivity(),
    OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private var location: Location? = null
    private var currentMarker: Marker? = null
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    lateinit var buttonClose: ImageButton
    lateinit var cardRoomReview: CardView

    companion object {
        const val REQUEST_PERMISSION_CODE = 101
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        init()
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                REQUEST_PERMISSION_CODE
            )
        } else {
            getLastLocation()
        }
    }

    private fun init() {
        buttonClose = findViewById(R.id.buttonClose)
        cardRoomReview = findViewById(R.id.reviewContainer)
        cardRoomReview.visibility = View.INVISIBLE
        buttonClose.setOnClickListener {
            onBackPressed()
        }
    }

    private fun getLastLocation() {
        fusedLocationProviderClient.lastLocation.addOnSuccessListener {
            location = it
            val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
            mapFragment.getMapAsync(this)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_PERMISSION_CODE -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    getLastLocation()
                } else {
                    Log.d("location", "Permission has been denied")
                }
                return
            }
            else -> {
                Log.d("location", "Permission has been denied")
            }
        }
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.setOnMapLoadedCallback {
            location?.let {
                val current = LatLng(it.latitude, it.longitude)
                val locations: MutableList<LatLng> = ArrayList()
                locations.add(current)
                locations.add(LatLng(21.0124, 105.8992))
                locations.add(LatLng(21.0110, 105.8931))
                locations.add(LatLng(21.0055, 105.8953))
                locations.add(LatLng(20.9788, 105.9600))
                locations.add(LatLng(21.0014, 105.8984))
                for (latLng in locations) {
                    mMap.addMarker(
                        MarkerOptions()
                            .position(latLng)
                            .icon(BitmapDescriptorFactory.fromBitmap(createMarker(R.layout.layout_map_marker)))
                    ).tag = "test"
                }

                val builder = LatLngBounds.Builder()
                builder.include(locations[0]) //điểm A

                builder.include(locations[locations.size - 1]) //điểm B

                val bounds = builder.build()
                val cu = CameraUpdateFactory.newLatLngBounds(bounds, 200)
                mMap.moveCamera(cu)
                mMap.animateCamera(CameraUpdateFactory.zoomTo(13f), 2000, null)
            }
        }

        mMap.setOnMarkerClickListener {
            val t: String = it.tag as String
            if (t == "test") {
                if (currentMarker == null) {
                    currentMarker = it
                } else {
                    currentMarker!!.setIcon(BitmapDescriptorFactory.fromBitmap(createMarker(R.layout.layout_map_marker)))
                }
                it.setIcon(BitmapDescriptorFactory.fromBitmap(createMarker(R.layout.layout_map_marker_selected)))
                cardRoomReview.visibility = View.VISIBLE
                currentMarker = it
            }
            false
        }

    }

    private fun createMarker(layout: Int): Bitmap {
        val marker = LayoutInflater.from(this).inflate(layout, null)
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        marker.layoutParams = ViewGroup.LayoutParams(52, ViewGroup.LayoutParams.WRAP_CONTENT)
        marker.measure(displayMetrics.widthPixels, displayMetrics.heightPixels)
        marker.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels)
        marker.buildLayer()
        val bitmap = Bitmap.createBitmap(
            marker.measuredWidth,
            marker.measuredHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        marker.draw(canvas)
        return bitmap
    }

}
