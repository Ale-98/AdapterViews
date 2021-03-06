package it.uninsubria.pdm.todoapp

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import java.util.*
import kotlin.collections.ArrayList

class DBHelper(context: Context):SQLiteOpenHelper(context, DBContract.DATABASE_NAME, null, DBContract.DATABASE_VERSION) {

    // Tag variabile statica
    companion object {
        val TAG = "DatabaseHelper"

        // Queries
        private val SQL_CREATE_ENTRIES =
            "create table "+DBContract.TodoTable.TABLE_NAME+"("+
                    DBContract.TodoTable.KEY_ID+" integer primary key autoincrement,"+
                    DBContract.TodoTable.KEY_TASK+" text not null,"+
                    DBContract.TodoTable.KEY_DATE+" integer,"+
                    DBContract.TodoTable.KEY_DEADLINE+" integer,"+
                    DBContract.TodoTable.KEY_IMAGE_URL+" text);"
        val SQL_SELECT_ALL = "select * from "+DBContract.TodoTable.TABLE_NAME
        private val SQL_DELETE_ENTRIES = "drop table if exists "+DBContract.TodoTable.TABLE_NAME
    }

    // Richiesto da SQLiteOpenHelper per creare il DB
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_ENTRIES)
        Log.d(TAG, "onCreate():create table")
    }

    // ''' per aggioranre il DB in caso di cambio versione(upgrade)
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int){
        db.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }

    // ''' per aggiornare il DB in caso di cambio versione(downgrade)
    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }

}