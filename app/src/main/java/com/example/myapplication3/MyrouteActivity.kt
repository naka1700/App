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
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

class MyrouteActivity : AppCompatActivity() , OnMapReadyCallback {
    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private var location: LatLng? = null
    private var tappedLocations: MutableList<LatLng> = mutableListOf() // マーカー位置を保持するプロパティ

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_myroute)
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        val sendButton = findViewById<Button>(R.id.ホームに戻る_button)
        // lambda式
        sendButton.setOnClickListener { v: View? ->
            val intent = Intent(application, HomeActivity::class.java)
            startActivity(intent)
        }

        val executionMarkersButton = findViewById<Button>(R.id.実行_button)
        executionMarkersButton.setOnClickListener {
            execution()
        }
    }
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        //以下マップピンのコード
        location = LatLng(34.98507661036779, 135.75255078869654)
        /*mMap.addMarker(MarkerOptions().position(location).title("Marker in 京都コンピュータ学院"))*/
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 17.5F));


    }
    private fun execution() {  // 保存ボタンを押したときの処理
        // 確認のダイアログを表示
        val mDocRef =FirebaseFirestore.getInstance().collection("コレクション名").document("1PkHI00AlzPutv2RdUH").get()
        val document =mDocRef.getResult()

        var tapped = document.get("フィールド名") as MutableList<LatLng>
        for (rang in 1..tapped.size) {
            val markerOptions = MarkerOptions()
                .position(tapped[rang])
                .title("Marker ${tapped[rang]}") // タイトルを設定します
            mMap.addMarker(markerOptions)
        }
    }
}