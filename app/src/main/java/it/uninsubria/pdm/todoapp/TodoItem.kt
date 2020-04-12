package it.uninsubria.pdm.todoapp

import android.content.ContentValues
import java.text.SimpleDateFormat
import java.util.*

class TodoItem() {

    var createOn:GregorianCalendar = GregorianCalendar()
    var todo:String = ""
    var id:Long = -1

    constructor(todo:String):this(){
        this.todo = todo
    }

    fun getAsContentValues():ContentValues{
        val cv = ContentValues()
        cv.put(DBContract.TodoTable.KEY_ID, id)
        cv.put(DBContract.TodoTable.KEY_TASK, todo)
        cv.put(DBContract.TodoTable.KEY_DATE, createOn.timeInMillis)
        return cv
    }

    @Override
    override fun toString():String {
        val currentDate:String = SimpleDateFormat("dd/MM/yyyy", Locale.ITALIAN).format(createOn.getTime());
        return currentDate + ":\n >> " + todo;
    }
}