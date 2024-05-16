package com.example.mapkit

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ToggleButton
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.location.Location
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.InputListener
import com.yandex.mapkit.map.Map
import com.yandex.mapkit.map.MapObjectCollection
import com.yandex.mapkit.mapview.MapView
import com.yandex.mapkit.user_location.UserLocationLayer

class MainActivity : AppCompatActivity() {
    private lateinit var mapView: MapView
    private lateinit var locButton: FloatingActionButton
    private lateinit var toggleButton: ToggleButton
    lateinit var userLocationLayer: UserLocationLayer
    lateinit var mapObjectCollection: MapObjectCollection

    private val addInputListener = (object : InputListener {
        override fun onMapTap(map: com.yandex.mapkit.map.Map, point: Point) {
            addPlacemarkOnMap(point)
        }

        override fun onMapLongTap(map: Map, point: Point) {
            addPlacemarkOnMap(point)
        }
    })
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MapKitFactory.setApiKey("cb08995e-704e-41f0-be47-d1cf616c8a3d")
        setContentView(R.layout.activity_main)

        MapKitFactory.initialize(this)
        setContentView(R.layout.activity_main)
        mapView = findViewById(R.id.mapview)
        Location()

        mapObjectCollection = mapView.map.mapObjects.addCollection()

        mapView.map.addInputListener(addInputListener)

        var trafficLayer = MapKitFactory.getInstance().createTrafficLayer(mapView!!.mapWindow)
        toggleButton =  findViewById(R.id.toggleBtn)
        toggleButton.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                trafficLayer.isTrafficVisible = true
            } else {
                trafficLayer.isTrafficVisible = false
            }
        }

    }

    fun addPlacemarkOnMap(point: Point) {
        mapObjectCollection.addPlacemark(point)
    }

    fun Location() {
        val locationPermissionRequest = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions.getOrDefault(android.Manifest.permission.ACCESS_FINE_LOCATION, false) -> {

                    locButton = findViewById(R.id.location)

                    userLocationLayer = MapKitFactory.getInstance().createUserLocationLayer(mapView.mapWindow)
                    userLocationLayer.isVisible = true
                    locButton.setOnClickListener() {
                        mapView.map.move(
                            CameraPosition(userLocationLayer.cameraPosition()!!.getTarget(), 13.0f, 0.0f, 0.0f),
                            Animation(Animation.Type.SMOOTH, 2F),
                            null
                        )
                    }
                }
                permissions.getOrDefault(android.Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                } else -> {
            }
            }
        }
        locationPermissionRequest.launch(arrayOf(
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION))
    }


    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart()
        mapView.onStart()
    }

    override fun onStop() {
        mapView.onStop()
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }
}