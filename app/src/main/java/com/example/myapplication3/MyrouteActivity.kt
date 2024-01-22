package com.example.myapplication3

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication3.databinding.ActivityMapsBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot


class MyrouteActivity : AppCompatActivity() , OnMapReadyCallback {
    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private var location: LatLng? = null

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
        var message = intent.getStringExtra("Message_KEY").toString()
        Log.d("GG",message)
        val executionMarkersButton = findViewById<Button>(R.id.実行_button)
        executionMarkersButton.setOnClickListener {
            execution(message)
        }

    }
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        //以下マップピンのコード
        location = LatLng(34.98507661036779, 135.75255078869654)
        /*mMap.addMarker(MarkerOptions().position(location).title("Marker in 京都コンピュータ学院"))*/
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 17.5F));
        Log.i(String.toString(),"k")


    }
    private fun execution(message: String) {
        if(message==null||message==""||message=="null"){
            //ホーム画面から直接マイルートを開くとこれになる
            //val mDocRef =FirebaseFirestore.getInstance().collection("コレクション名").document("1PkHI00AlzPutv2RdUH")
            val db: FirebaseFirestore = FirebaseFirestore.getInstance()
            val pProfileRef: CollectionReference = db.collection("コレクション名")
            pProfileRef.get().addOnCompleteListener(object : OnCompleteListener<QuerySnapshot?> {

                override fun onComplete(@NonNull task: Task<QuerySnapshot?>) {
                    if (task.isSuccessful) {
                        for (document in task.result!!) {
                            Log.d(TAG, document.id + " => " + document.data)
                            Log.d("TAG", document::class.java.toString())
                           // val qds = document as QueryDocumentSnapshot
                            //クラウドから取り出したdocumentの型をLISTにする
                            val polyPoint = document.get("フィールド名") as List<*>
                            val length: Int = polyPoint.size
                            if (length == 0) {
                                return
                            }
                            //val poly = PolygonOptions()
                            val markerOptions = MarkerOptions()
                            if(polyPoint.size>=2) {
                                val polylineOptions = PolylineOptions()
                                    .width(5f)

                                for (i in 0 until length) {
                                    var dt = polyPoint[i].toString()
                                    var da = dt.split("=", ",", "}")
                                    var b = da[1].toDouble()
                                    var c = da[3].toDouble()
                                    var la: LatLng = LatLng(b, c)
                                    markerOptions.position(la)
                                    polylineOptions.add(la)
                                    Log.d("TAG", i.toString())
                                    mMap.addMarker(markerOptions)
                                }
                                mMap.addPolyline(polylineOptions)
                            }
                            break
                        }
                    } else {
                        Log.d(TAG, "Error getting documents:", task.getException())
                    }
                }
            })
        }else{//ルート検索から選ぶとこれになる
            Log.d("MESSAGE", "送られてきたメッセージは"+message)
            //val mDocRef =FirebaseFirestore.getInstance().collection("コレクション名").document("1PkHI00AlzPutv2RdUH")
            val db: FirebaseFirestore = FirebaseFirestore.getInstance()
            val pProfileRef: CollectionReference = db.collection("コレクション名")
            var pProfileRef2 = pProfileRef.document(message)
            pProfileRef2.get().addOnCompleteListener(object : OnCompleteListener<DocumentSnapshot?> {

                override fun onComplete(@NonNull task: Task<DocumentSnapshot?>) {
                    if (task.isSuccessful) {
                        var document = task.result
                        Log.d(TAG, "入力されています"+document.toString())
                            //クラウドから取り出したdocumentの型をLISTにする
                            val polyPoint = document?.get("フィールド名") as List<*>
                            val length: Int = polyPoint.size
                            if (length == 0) {
                                return
                            }
                            //val poly = PolygonOptions()
                            val markerOptions = MarkerOptions()
                            if(polyPoint.size>=2) {
                                val polylineOptions = PolylineOptions()
                                    .width(5f)
                                for (i in 0 until length) {
                                    var dt = polyPoint[i].toString()
                                    var da = dt.split("=", ",", "}")
                                    var b = da[1].toDouble()
                                    var c = da[3].toDouble()
                                    var la: LatLng = LatLng(b, c)
                                    markerOptions.position(la)
                                    polylineOptions.add(la)
                                    Log.d("TAG", i.toString())
                                    mMap.addMarker(markerOptions)
                                }
                                mMap.addPolyline(polylineOptions)
                            }
                    } else {
                        Log.d(TAG, "Error getting documents:", task.getException())
                    }
                }
            })

        }

        }
        //val r = mDocRef.get().result
        //Log.d("TAG",r.toString())
      //  Log.d("TAG", r::class.java.toString())
//        val pos = r as com.google.android.gms.tasks.zzw

          //  Log.d("TAG","ここはOK2 kotlin latlng firestore")
        //    for (rang in 0..2) {
           // val markerOptions = MarkerOptions()
             //   .position(tappedLocations)
              //  .title("Marker ${rang}") // タイトルを設定します
            //mMap.addMarker(markerOptions)
           // val intent = Intent(application, HomeActivity::class.java)
            //startActivity(intent)
         //       Log.i(String.toString(),rang.toString())
             //   mMap.addMarker(markerOptions)

        }
            //.result as MutableList<LatLng>
            /*for (rang in 0..2) {
                val markerOptions = MarkerOptions()
                    .position(tappedLocations[rang] as LatLng)
                    .title("Marker ${tappedLocations[rang]}") // タイトルを設定します
               mMap.addMarker(markerOptions)
                val intent = Intent(application, HomeActivity::class.java)
                startActivity(intent)
            }*/

  //  }
//}
