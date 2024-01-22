package com.example.myapplication3

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore

import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class SearchActivity : AppCompatActivity() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    var array2:MutableList<String> = mutableListOf()
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        val sendButton = findViewById<Button>(R.id.ホームに戻る_button)
        // lambda式
        sendButton.setOnClickListener { v: View? ->
            val intent = Intent(application, HomeActivity::class.java)
            startActivity(intent)
        }
        val zikkouButton = findViewById<Button>(R.id.検索_button)
        zikkouButton.setOnClickListener {
            execution()

        }

        // 位置情報クライアントの初期化
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)


        val array: MutableList<String> = mutableListOf()


        var i2:String
        val db: FirebaseFirestore = FirebaseFirestore.getInstance()
        val pProfileRef: CollectionReference = db.collection("コレクション名")
        pProfileRef.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                for (document in task.result!!) {
                    Log.d(ContentValues.TAG, document.id)

                    i2 = document.id.toString()
                    Log.d("TAG", i2::class.qualifiedName.toString())
                    Log.d("TAG", i2)
                    array.add(i2)
                }

                val length = array.size
                Log.d("TAG", array::class.qualifiedName.toString())
                for (i in 0 until length) {
                    Log.d(ContentValues.TAG, array[i])
                    array2.add(array[i])
                }
            }
        }
        val listView =findViewById<ListView>(R.id.List_view)
        listView.adapter = ArrayAdapter(this,android.R.layout.simple_list_item_1,array2)

}

    private fun execution() {
        val listView =findViewById<ListView>(R.id.List_view)
        Log.d(ContentValues.TAG, "aaaaaaaaaaaa" )
        listView.adapter = ArrayAdapter(this,android.R.layout.simple_list_item_1,array2)
        listView.onItemClickListener = ListItemClick()

    }
   private inner class ListItemClick : AdapterView.OnItemClickListener {

       override fun onItemClick(parent: AdapterView<*>, view: View, position: Int, id: Long) {
           var item = parent.getItemAtPosition(position)
           Log.d("TAG", item::class.qualifiedName.toString())
           Log.d("TAG", item.toString())
           sendmessage(item)
       }

       private fun sendmessage(item: Any) {
           val intent =Intent(application,MyrouteActivity::class.java)
           val text =item.toString()
           intent.putExtra("Message_KEY",text)
           startActivity(intent)
       }

   }
    /*
    listView.setOnClickListener { position ->
           // val intent =Intent(this,MyrouteActivity::class.java)
            val text =array2[position.toString().toInt()]
            Log.d("TAG", "ここは"+text)
           // intent.putExtra("Test_KEY",text)
            //startActivity(intent)
        }
     */
}


