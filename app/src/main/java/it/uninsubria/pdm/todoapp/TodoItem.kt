package it.uninsubria.pdm.todoapp

import android.content.ContentValues
import java.text.SimpleDateFormat
import java.util.*

class TodoItem() {

    // Campi dei TodoItems
    var createOn:GregorianCalendar = GregorianCalendar()
    var todo:String = ""
    var id:Long = -1
    var imgUrl:String = ""
    var deadline : GregorianCalendar = GregorianCalendar()
    fun dateToString(date: GregorianCalendar): String {
        return SimpleDateFormat("dd/MM/yyyy", Locale.ITALIAN).format(date.getTime())
    }

    constructor(todo:String):this(){
        this.todo = todo
    }

    // Per ottenere un todoItem travestito da ContentValues, necessario per aggiunta di un TodoItem al DB
    fun getAsContentValues():ContentValues{
        val cv = ContentValues()
        cv.put(DBContract.TodoTable.KEY_ID, id)
        cv.put(DBContract.TodoTable.KEY_TASK, todo)
        cv.put(DBContract.TodoTable.KEY_DATE, createOn.timeInMillis)
        cv.put(DBContract.TodoTable.KEY_DEADLINE, deadline.timeInMillis)
        cv.put(DBContract.TodoTable.KEY_IMAGE_URL, imgUrl)
        return cv
    }

    @Override
    override fun toString():String {
        val currentDate:String = SimpleDateFormat("dd/MM/yyyy", Locale.ITALIAN).format(createOn.getTime());
        return currentDate + ":\n >> " + todo;
    }
}