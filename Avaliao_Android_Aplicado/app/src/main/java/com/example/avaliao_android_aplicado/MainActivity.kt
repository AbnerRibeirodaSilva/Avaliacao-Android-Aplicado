package com.example.avaliao_android_aplicado

import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract.Data
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.avaliao_android_aplicado.database.DatabaseHandler

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val tipoSpinner: Spinner = findViewById(R.id.spinner1)
        val detalheSpinner: Spinner = findViewById(R.id.spinner2)
        val etValor: EditText = findViewById(R.id.etValor)
        val etData: EditText = findViewById(R.id.etData)
        val btLancar: Button = findViewById(R.id.btLancar)
        val btLancamentos: Button = findViewById(R.id.btLancamentos)
        val btSaldo: Button = findViewById(R.id.btSaldo)





        val tipos = arrayOf("Crédito", "Débito")
        val tipoAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, tipos)
        tipoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        tipoSpinner.adapter = tipoAdapter


        val detalhesCredito = arrayOf("Salário", "Extras")
        val detalhesDebito = arrayOf("Alimentação", "Transporte", "Saúde", "Moradia")


        tipoSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedTipo = parent.getItemAtPosition(position).toString()

                val detalhesAdapter = when (selectedTipo) {
                    "Crédito" -> ArrayAdapter(this@MainActivity, android.R.layout.simple_spinner_item, detalhesCredito)
                    "Débito" -> ArrayAdapter(this@MainActivity, android.R.layout.simple_spinner_item, detalhesDebito)
                    else -> ArrayAdapter(this@MainActivity, android.R.layout.simple_spinner_item, arrayOf<String>())
                }
                detalhesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                detalheSpinner.adapter = detalhesAdapter
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }



        }

        btLancar.setOnClickListener {
            val tipo = tipoSpinner.selectedItem.toString()
            val detalhe = detalheSpinner.selectedItem.toString()
            val valor = etValor.text.toString().toDouble()
            val data = etData.text.toString()

            val db = DatabaseHandler(this)
            db.addLancamento(tipo, detalhe, valor, data)

            Toast.makeText(this, "Lançamento salvo com sucesso!", Toast.LENGTH_SHORT).show()
        }

        btLancamentos.setOnClickListener {
            val intent = Intent(this, LancamentosActivity::class.java)
            startActivity(intent)
        }

        btSaldo.setOnClickListener {
            val dbHelper = DatabaseHandler(this)
            val saldo = dbHelper.getSaldo()

            val dialogBuilder = AlertDialog.Builder(this)
            dialogBuilder.setMessage("Saldo: R$ $saldo")
                .setCancelable(true)
                .setPositiveButton("OK") { dialog, _ ->
                    dialog.dismiss()
                }

            val alert = dialogBuilder.create()
            alert.setTitle("Saldo Atual")
            alert.show()
        }

}}