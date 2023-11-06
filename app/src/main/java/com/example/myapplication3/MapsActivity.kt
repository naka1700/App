package com.example.myapplication3

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication3.databinding.ActivityMapsBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.util.*


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private var location: LatLng? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     * 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
     * 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
     * あいうえお
     * かきくけこ
     * さしすせそ
     * たちつてと
     * なにぬねの
     * はひふへほ
     * wawon
     * aaaaaaaa
     *
     * マップアプリ作成
     *
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap


        // Add a marker in Sydney and move the camera
        location = LatLng(34.98507661036779, 135.75255078869654)
        mMap.addMarker(MarkerOptions().position(location).title("Marker in 京都コンピュータ学院"))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 17.5F));

        mMap.setOnMapClickListener { tapLocation: LatLng ->
            // tapされた位置の緯度経度
            location = LatLng(tapLocation.latitude, tapLocation.longitude)
            val str: String = java.lang.String.format(
                Locale.US,
                "%f, %f",
                tapLocation.latitude,
                tapLocation.longitude
            )
            mMap.addMarker(MarkerOptions().position(location).title(str))
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 17.5f))
        }

        mMap.setOnMapLongClickListener { longpushLocation: LatLng ->
            val newlocation = LatLng(longpushLocation.latitude, longpushLocation.longitude)
            mMap.addMarker(
                MarkerOptions().position(newlocation)
                    .title("" + longpushLocation.latitude + " :" + longpushLocation.longitude)
            )
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(newlocation, 17.5f))
        }

        val cUpdate = CameraUpdateFactory.newLatLngZoom(
            LatLng(34.98507661036779, 135.75255078869654), 17.5f
        )
        mMap.moveCamera(cUpdate)


    }


}