package it.uninsubria.pdm.todoapp

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.util.Log
import java.util.*
import kotlin.collections.ArrayList

class DBAdapter {

    // Istanza di DBHelper
    private var theHelper:DBHelper

    // Costruttore per inizializzare il DBHelper
    constructor(context: Context){
        theHelper = DBHelper(context)
    }

    companion object{
        val TAG = "DBAdapter"
    }

    // Per inserimento in DB
    fun insertItem(item:TodoItem):Long{
        val db = theHelper.writableDatabase
        var content: ContentValues = item.getAsContentValues()
        item.id = db.insert(DBContract.TodoTable.TABLE_NAME, null, content) // Essendoci autoincrement l'id è creato dal db e restituito dalla insert
        db.close()
        Log.d(DBHelper.TAG, "inserted item: $item")
        return item.id
    }

    // Per ottenere tutti gli elementi dal DB
    fun getAllItems():Collection<TodoItem>{
        var items:ArrayList<TodoItem> = ArrayList()

        val db = theHelper.writableDatabase
        // Per esecuzione query testuale passata come primo parametro
        val cursor: Cursor = db.rawQuery(DBHelper.SQL_SELECT_ALL, null)
        if(cursor.moveToFirst()){ // Faccio scorrere il cursore per ottenere tutti i dati dal result set
            do{
                var task = TodoItem()
                task.id = cursor.getString(0).toLong()
                task.todo = cursor.getString(1)
                val createOn = GregorianCalendar()
                createOn.setTimeInMillis(cursor.getLong(2))
                task.createOn = createOn
                //task.deadline = GregorianCalendar(cursor.getInt(3)))
                task.imgUrl = cursor.getString(4)
                items.add(0, task) // Aggiungo l'item alla lista di item da ritornare
            }while(cursor.moveToNext())
        }
        db.close()
        Log.d(DBHelper.TAG, "getAllItems():$items")
        return items
    }

    // Per eliminare in Item dal DB
    fun deleteItem(item:TodoItem):Boolean{
        val db = theHelper.writableDatabase
        // Paramentri: delete(tabella, chiave, valore)
        val deleted =  db.delete(DBContract.TodoTable.TABLE_NAME, DBContract.TodoTable.KEY_ID+" = ?", arrayOf(item.id.toString())) == 1
        db.close()
        Log.d(DBHelper.TAG, "deleted item: $item")
        return deleted
    }

    // Per ottenere un resultSet contenente tutti i dati della tabella indicata
    fun getAllEntries():Cursor{
        val db = theHelper.readableDatabase
        return db.query(DBContract.TodoTable.TABLE_NAME, null, null, null, null, null, null);
    }

}