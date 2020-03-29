package it.uninsubria.pdm.todoapp

import java.text.SimpleDateFormat
import java.util.*

class TodoItem(todo:String) {

    private val createOn:GregorianCalendar = GregorianCalendar()
    private val todo:String = todo

    @Override
    override fun toString():String {
        val currentDate:String = SimpleDateFormat("dd/MM/yyyy", Locale.ITALIAN).format(createOn.getTime());
        return currentDate + ":\n >> " + todo;
    }
}