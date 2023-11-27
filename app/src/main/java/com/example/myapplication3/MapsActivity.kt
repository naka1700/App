package com.example.myapplication3

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication3.databinding.ActivityMapsBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import java.util.*


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private var location: LatLng? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val sendButton = findViewById<Button>(R.id.ホームに戻る_button)

        sendButton.setOnClickListener { v: View? ->
            val intent = Intent(application, HomeActivity::class.java)
            startActivity(intent)
        }


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
     * おおおお
     *
     * マップアプリ作成
     *
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        //以下マップピンのコード
        location = LatLng(34.98507661036779, 135.75255078869654)
        mMap.addMarker(MarkerOptions().position(location).title("Marker in 京都コンピュータ学院"))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 17.5F));


        /** //tap
        mMap.setOnMapClickListener { tapLocation: LatLng ->
            // tapされた位置の緯度経度
            location = LatLng(tapLocation.latitude, tapLocation.longitude)
            val pin: String = java.lang.String.format(
                Locale.US,
                "%f, %f",
                tapLocation.latitude,
                tapLocation.longitude
            )
            mMap.addMarker(MarkerOptions().position(location).title(pin))
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 17.5f))

            // タップされた場所のリストを定義
            val tappedLocations: MutableList<LatLng> = mutableListOf()

// マップのクリックリスナーを設定
            mMap.setOnMapClickListener { tapLocation ->
                // タップされた場所をリストに追加
                tappedLocations.add(tapLocation)

                // ポイントが2つ以上ある場合、ポリラインを描画
                if (tappedLocations.size >= 2) {
                    // 前のポリラインを削除する場合はここにコメントを外してください
                    // mMap.clear()

                    // ポリラインの属性（色や幅など）を設定するためのPolylineOptionsを作成
                    val polylineOptions = PolylineOptions()
                        .width(5f) // ポリラインの幅をピクセル単位で設定

                    // タップされた場所をポリラインに追加
                    for (location in tappedLocations) {
                        polylineOptions.add(location)
                    }

                    // ポリラインを地図に追加
                    mMap.addPolyline(polylineOptions)
                }
            }*/

// タップされた位置を格納するリストを定義します
val tappedLocations: MutableList<LatLng> = mutableListOf()

// マップクリックのリスナーを設定します
            mMap.setOnMapClickListener { tapLocation ->
                // タップされた位置をリストに追加します
                tappedLocations.add(tapLocation)

                // マーカーを作成してマップに追加します
                val markerOptions = MarkerOptions()
                    .position(tapLocation)
                    .title("Marker ${tappedLocations.size}") // タイトルを設定します
                mMap.addMarker(markerOptions)

                // リストに少なくとも2つのポイントがある場合、ポリラインを描画します
                if (tappedLocations.size >= 2) {
                    // 前のポリラインを削除する必要がある場合は削除します
                    // mMap.clear() // 前のポリラインを消去したい場合はこの行のコメントを外してください

                    // ポリラインの属性（色、幅など）を設定するための PolylineOptions を作成します
                    val polylineOptions = PolylineOptions()
                        .width(5f) // ポリラインの幅をピクセルで設定します

                    // 全てのタップされた位置をポリラインに追加します
                    for (location in tappedLocations) {
                        polylineOptions.add(location)
                    }

                    // マップにポリラインを追加します
                    mMap.addPolyline(polylineOptions)
                }
            }





        }


/*longtap
        mMap.setOnMapLongClickListener { longpushLocation: LatLng ->
            val newlocation = LatLng(longpushLocation.latitude, longpushLocation.longitude)
            mMap.addMarker(
                MarkerOptions().position(newlocation)
                    .title("" + longpushLocation.latitude + " :" + longpushLocation.longitude)
            )
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(newlocation, 17.5f))
        }*/

            /**val cUpdate = CameraUpdateFactory.newLatLngZoom(
                LatLng(34.98507661036779, 135.75255078869654), 17.5f
            )
            mMap.moveCamera(cUpdate)*/


        }



