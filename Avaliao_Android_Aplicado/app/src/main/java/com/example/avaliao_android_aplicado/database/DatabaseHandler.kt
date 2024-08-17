package com.example.avaliao_android_aplicado.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHandler (context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "fluxoDeCaixa.db"
        private const val TABLE_NAME = "lancamentos"
        private const val COLUMN_ID = "id"
        private const val COLUMN_TIPO = "tipo"
        private const val COLUMN_DETALHE = "detalhe"
        private const val COLUMN_VALOR = "valor"
        private const val COLUMN_DATA = "data_lanc"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = ("CREATE TABLE $TABLE_NAME ("
                + "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "$COLUMN_TIPO TEXT,"
                + "$COLUMN_DETALHE TEXT,"
                + "$COLUMN_VALOR REAL,"
                + "$COLUMN_DATA TEXT)")
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun addLancamento(tipo: String, detalhe: String, valor: Double, data: String) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COLUMN_TIPO, tipo)
        values.put(COLUMN_DETALHE, detalhe)
        values.put(COLUMN_VALOR, valor)
        values.put(COLUMN_DATA, data)

        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    fun getAllLancamentos(): Cursor {
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM $TABLE_NAME", null)
    }

    fun getSaldo(): Double {
        val db = this.readableDatabase
        var totalCredito = 0.0
        var totalDebito = 0.0

        val cursorCredito = db.rawQuery("SELECT SUM(valor) FROM $TABLE_NAME WHERE tipo = 'Crédito'", null)
        if (cursorCredito.moveToFirst()) {
            totalCredito = cursorCredito.getDouble(0)
        }
        cursorCredito.close()

        val cursorDebito = db.rawQuery("SELECT SUM(valor) FROM $TABLE_NAME WHERE tipo = 'Débito'", null)
        if (cursorDebito.moveToFirst()) {
            totalDebito = cursorDebito.getDouble(0)
        }
        cursorDebito.close()

        return totalCredito - totalDebito
    }
}