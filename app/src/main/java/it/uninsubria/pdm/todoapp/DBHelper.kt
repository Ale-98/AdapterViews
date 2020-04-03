package it.uninsubria.pdm.todoapp

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import java.util.*
import kotlin.collections.ArrayList

class DBHelper(context:Context):SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        val TAG = "DatabaseHelper"
        val DATABASE_VERSION = 1 // Per capire se deve essere eseguito onUpgrade o onDowngrade in base alla varsione del db
        val DATABASE_NAME = "TodoList.db" // Tutto il db starà all'interno di un file con questo nome

        val TABLE_NAME = "TodoItems"
        val KEY_ID = "id"
        val KEY_TASK = "task"
        val KEY_DATE = "creation_date"

        private val SQL_CREATE_ENTRIES =
            "create table $TABLE_NAME("+
                    "$KEY_ID integer primary key autoincrement,"+
                    "$KEY_TASK text not null,"+
                    "$KEY_DATE integer);"
        private val SQL_SELECT_ALL = "select * from $TABLE_NAME"
        private val SQL_DELETE_ENTRIES = "drop table if exists $TABLE_NAME"

        var theHelper:DBHelper? = null
        // Pattern Singleton(TODO--> rendere costruttore privato)
        fun getInstance(context:Context):DBHelper{
            if (theHelper == null) return DBHelper(context)
            else return theHelper as DBHelper
        }
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_ENTRIES)
        Log.d(TAG, "onCreate():create table")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int){
        db.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }

    fun getAllItems():Collection<TodoItem>{
        var items:ArrayList<TodoItem> = ArrayList()

        val db = this.writableDatabase
        val cursor:Cursor = db.rawQuery(SQL_SELECT_ALL, null)
        if(cursor.moveToFirst()){
            do{
                var task = TodoItem()
                task.id = cursor.getString(0).toLong()
                task.todo = cursor.getString(1)
                val createOn = GregorianCalendar()
                createOn.setTimeInMillis(cursor.getLong(2))
                task.createOn = createOn
                items.add(0, task)
            }while(cursor.moveToNext())
        }
        db.close()
        Log.d(TAG, "getAllItems():$items")
        return items
    }
    fun deleteItem(item:TodoItem){
        val db = writableDatabase
        db.delete(TABLE_NAME, "$KEY_ID = ?", arrayOf(item.id.toString()))
        db.close()
        Log.d(TAG, "deleted item: $item")
    }
    fun insertItem(item:TodoItem):Long{
        val db = writableDatabase
        var content: ContentValues = ContentValues()
        content.put(KEY_TASK, item.todo)
        content.put(KEY_DATE, item.createOn.timeInMillis)
        item.id = db.insert(TABLE_NAME, null, content) // Essendoci autoincrement l'id è creato dal db e restituito dalla insert
        db.close()
        Log.d(TAG, "inserted item: $item")
        return item.id
    }

}