package com.example.avaliao_android_aplicado

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.example.avaliao_android_aplicado.database.DatabaseHandler

class LancamentosActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lancamentos)


        val lv = findViewById<ListView>(R.id.lvLancamentos)
        val db = DatabaseHandler(this)
        val cursor = db.getAllLancamentos()

        val lancamentosList = ArrayList<String>()

        if (cursor.moveToFirst()) {
            do {
                val tipo = cursor.getString(cursor.getColumnIndexOrThrow("tipo"))
                val detalhe = cursor.getString(cursor.getColumnIndexOrThrow("detalhe"))
                val valor = cursor.getDouble(cursor.getColumnIndexOrThrow("valor"))
                val data = cursor.getString(cursor.getColumnIndexOrThrow("data_lanc"))
                lancamentosList.add("$tipo | $detalhe | $valor | $data")
            } while (cursor.moveToNext())
        }

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, lancamentosList)

        lv.adapter = adapter

    }
}
