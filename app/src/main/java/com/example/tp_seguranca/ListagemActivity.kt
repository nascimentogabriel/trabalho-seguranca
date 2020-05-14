package com.example.tp_seguranca


import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.annotation.RequiresApi
import kotlinx.android.synthetic.main.activity_listagem.*
import java.io.File


class ListagemActivity : AppCompatActivity() {

    private lateinit var listView : ListView

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listagem)

        listView = findViewById<ListView>(R.id.listArquivos)



        val list = Environment.getExternalStorageState()
        textView2.text = list
        //getExternalFilesDirs("$dataehr.crd")

//        val listitem = arrayOfNulls<File>(list)
//
//        for (i in 0 until list.size){
//            val item = list[i]
//            listitem[i] = item
//        }
//        val adapter = ArrayAdapter(this,android.R.layout.simple_list_item_1,listitem)
//        listView.adapter = adapter
         }

}
