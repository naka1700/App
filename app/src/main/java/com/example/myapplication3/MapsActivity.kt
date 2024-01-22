package com.example.myapplication3

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication3.databinding.ActivityMapsBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private var location: LatLng? = null
    private val tappedLocations: MutableList<LatLng> = mutableListOf() // マーカー位置を保持するプロパティ
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    val db = Firebase.firestore//クラウドに保存するためのプロパティ



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
        val removeMarkersButton = findViewById<Button>(R.id.削除_button)
        removeMarkersButton.setOnClickListener {
            undoLastAction()
        }

        val seveMarkersButton = findViewById<Button>(R.id.保存_button)
        seveMarkersButton.setOnClickListener {
            seve()
        }

        // 位置情報クライアントの初期化
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.isMyLocationEnabled = true


        /*
        //以下マップピンのコード
        location = LatLng(34.98507661036779, 135.75255078869654)
        /*mMap.addMarker(MarkerOptions().position(location).title("Marker in 京都コンピュータ学院"))*/
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 17.5F));*/

        /*位置情報権限の習得*/

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                1
            )
            return
        }
        /*位置情報の習得と初期位置*/
        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            location?.let {
                val currentLatLng = LatLng(location.latitude, location.longitude)
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 17.5f))
            }
        }

        // 以下マップピン
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


   /* // 逆順でマップピンを削除する関数
    private fun removeMarkersAndPolylines() {
        // ポリラインを削除します
          mMap.clear()
          tappedLocations.clear()

       // tappedLocations リストから逆順でマーカーを削除します
        for (i in tappedLocations.size - 1 downTo 0) {
            // マーカーを削除します
            val removedMarker = mMap.addMarker(MarkerOptions().position(tappedLocations[i]))
            removedMarker.remove() // マーカーをマップから削除します
        }
    }*/



    // 以下直前の操作の取り消し
    private fun undoLastAction() {
        // リストに少なくとも1つのポイントがある場合のみ、直前の操作を取り消します
        if (tappedLocations.isNotEmpty()) {
            val lastIndex = tappedLocations.size - 1

            // 直前に追加されたマーカーを削除します
            mMap.clear() // マップ上の全てのオーバーレイ（マーカーとポリライン）を削除

            // 新しいマーカーとポリラインを描画します
            for (i in 0 until lastIndex) {
                val markerOptions = MarkerOptions()
                    .position(tappedLocations[i])
                    .title("Marker ${i + 1}")
                mMap.addMarker(markerOptions)
            }

            val polylineOptions = PolylineOptions()
                .width(5f)

            for (i in 0 until lastIndex) {
                polylineOptions.add(tappedLocations[i])
            }

            mMap.addPolyline(polylineOptions)

            // 直前の操作を取り消した後、最後の位置をリストから削除します
            tappedLocations.removeAt(lastIndex)
        }
    }


    // 以下保存機能
    private fun showSaveConfirmationDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("保存しますか？")
        builder.setMessage("変更を保存しますか？")
        builder.setPositiveButton("保存") { dialog, which ->
            saveToFirestore() // データを保存する処理を実行
        }
        builder.setNegativeButton("キャンセル") { dialog, which ->
            // キャンセルボタンが押された場合の処理
        }
        val dialog = builder.create()
        dialog.show()
    }

    private fun seve() {  // 保存ボタンを押したときの処理
        // 確認のダイアログを表示
        showSaveConfirmationDialog()
    }

    private fun saveToFirestore() {
        val mDocRef = FirebaseFirestore.getInstance().collection("コレクション名")
        val data: MutableMap<String, Any> = HashMap()
        data["フィールド名"] = tappedLocations
        mDocRef.add(data)

        // ホーム画面に戻る
        val intent = Intent(application, HomeActivity::class.java)
        startActivity(intent)
    }


        }



