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
import com.google.android.gms.maps.model.PolylineOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private var location: LatLng? = null
    private val tappedLocations: MutableList<LatLng> = mutableListOf() // マーカー位置を保持するプロパティ

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
     *マップ保存
     * マップアプリ作成
     *
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        //以下マップピンのコード
        location = LatLng(34.98507661036779, 135.75255078869654)
        /*mMap.addMarker(MarkerOptions().position(location).title("Marker in 京都コンピュータ学院"))*/
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 17.5F));

// タップされた位置を格納するリストを定義します


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



    // 直前の操作を取り消す
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
    private fun seve(){  //保存ボタンを押したときの処理

        val mDocRef = FirebaseFirestore.getInstance().collection("コレクション名")//コレクションの指定

        val data: MutableMap<String, Any> = HashMap()//フィールド名と保存する座標情報を入れるための変数
        data["フィールド名"] = tappedLocations//箱
        mDocRef.add(data)//データを加える

        //ホーム画面に戻る
        val intent = Intent(application, HomeActivity::class.java)
        startActivity(intent)
    }


        }



