package it.uninsubria.pdm.todoapp

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.util.Log
import java.util.*
import kotlin.collections.ArrayList

class DBAdapter {

    private var theHelper:DBHelper

    constructor(context: Context){
        theHelper = DBHelper(context)
    }

    companion object{
        val TAG = "DBAdapter"
    }

    fun insertItem(item:TodoItem):Long{
        val db = theHelper.writableDatabase
        var content: ContentValues = item.getAsContentValues()
        item.id = db.insert(DBContract.TodoTable.TABLE_NAME, null, content) // Essendoci autoincrement l'id è creato dal db e restituito dalla insert
        db.close()
        Log.d(DBHelper.TAG, "inserted item: $item")
        return item.id
    }

    fun getAllItems():Collection<TodoItem>{
        var items:ArrayList<TodoItem> = ArrayList()

        val db = theHelper.writableDatabase
        val cursor: Cursor = db.rawQuery(DBHelper.SQL_SELECT_ALL, null)
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
        Log.d(DBHelper.TAG, "getAllItems():$items")
        return items
    }
    fun deleteItem(item:TodoItem):Boolean{
        val db = theHelper.writableDatabase
        val deleted =  db.delete(DBContract.TodoTable.TABLE_NAME, DBContract.TodoTable.KEY_ID+" = ?", arrayOf(item.id.toString())) == 1
        db.close()
        Log.d(DBHelper.TAG, "deleted item: $item")
        return deleted
    }

    fun getAllEntries():Cursor{
        val db = theHelper.readableDatabase
        return db.query(DBContract.TodoTable.TABLE_NAME, null, null, null, null, null, null);
    }

}