package com.example.mapkit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ToggleButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.mapview.MapView

class MainActivity : AppCompatActivity() {
    private lateinit var mapView: MapView
    private lateinit var locButton: FloatingActionButton
    private lateinit var toggleButton: ToggleButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MapKitFactory.setApiKey("cb08995e-704e-41f0-be47-d1cf616c8a3d")
        setContentView(R.layout.activity_main)

        MapKitFactory.initialize(this)
        setContentView(R.layout.activity_main)
        mapView = findViewById(R.id.mapview)

        locButton = findViewById(R.id.location)
        locButton.setOnClickListener() {
            val (latitude, longitude) = GlavPochtamt()
            mapView.map.move(
                CameraPosition(Point(latitude, longitude), 18.0f, 0.0f, 0.0f),
                Animation(Animation.Type.SMOOTH, 2F),
                null
            )
        }

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

    fun GlavPochtamt(): Pair<Double, Double> {
        return Pair(55.35485859365324, 86.0863204841308)
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