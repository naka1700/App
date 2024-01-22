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
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore

class SearchActivity : AppCompatActivity() {
    var array2:MutableList<String> = mutableListOf()
    var selectMessage:String="";//追加
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
        val deleteButton = findViewById<Button>(R.id.削除_button)//追加
        deleteButton.setOnClickListener {
            delete(selectMessage)
        }
        val decisionButton = findViewById<Button>(R.id.決定_button)//追加
        decisionButton.setOnClickListener {
            sendmessage(selectMessage)
        }

        val array: MutableList<String> = mutableListOf()
        var i2:String
        val db: FirebaseFirestore = FirebaseFirestore.getInstance()
        val pProfileRef: CollectionReference = db.collection("コレクション名")
        pProfileRef.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                for (document in task.result!!) {
                    //Log.d(ContentValues.TAG, document.id)
                    i2 = document.id
                    //Log.d("TAG", i2::class.qualifiedName.toString())
                    //Log.d("TAG", i2)
                    array.add(i2)
                }

                val length = array.size
                // Log.d("TAG", array::class.qualifiedName.toString())
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
        listView.adapter = ArrayAdapter(this,android.R.layout.simple_list_item_1,array2)
        listView.onItemClickListener = ListItemClick()

    }
    private inner class ListItemClick : AdapterView.OnItemClickListener {

        override fun onItemClick(parent: AdapterView<*>, view: View, position: Int, id: Long) {
            var item = parent.getItemAtPosition(position)
            selectMessage=item.toString()//追加
            Log.d("TAG", "一時保存完了："+selectMessage)//
            //sendmessage(item)
        }

        /*private fun sendmessage(selectMessage: String) {
            val intent =Intent(application,MyrouteActivity::class.java)
            intent.putExtra("Message_KEY",selectMessage)
            startActivity(intent)
        }*/

    }
    private fun sendmessage(selectMessage: String) {//()
        val intent =Intent(application,MyrouteActivity::class.java)
        intent.putExtra("Message_KEY",selectMessage)
        startActivity(intent)
    }
    private fun delete(selectMessage: String) {  // 削除ボタンを押したときの処理　ここは丸ごと追加
        // 確認のダイアログを表示
        showDeleteConfirmationDialog(selectMessage)
    }
    private fun showDeleteConfirmationDialog(selectMessage: String) {//ここも丸互つ追加
        val builder = AlertDialog.Builder(this)
        builder.setTitle("削除しますか？")
        builder.setMessage("本当に削除しますか？")
        builder.setMessage(selectMessage)
        builder.setPositiveButton("削除") { dialog, which ->
            // データを削除する処理を実行
            val db: FirebaseFirestore = FirebaseFirestore.getInstance()
            val pProfileRef: CollectionReference = db.collection("コレクション名")
            pProfileRef.document(selectMessage).delete().addOnCompleteListener{
                Log.d("DELETE","削除完了")
                /*val intent = Intent(application, HomeActivity::class.java)
                startActivity(intent)*/
            }
        }
        builder.setNegativeButton("キャンセル") { dialog, which ->
            // キャンセルボタンが押された場合の処理
        }
        val dialog = builder.create()
        dialog.show()

    }
}





