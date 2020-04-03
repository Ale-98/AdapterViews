package it.uninsubria.pdm.todoapp

import java.text.SimpleDateFormat
import java.util.*

class TodoItem() {

    var createOn:GregorianCalendar = GregorianCalendar()
    var todo:String = ""
    var id:Long = -1

    constructor(todo:String):this(){
        this.todo = todo
    }

    @Override
    override fun toString():String {
        val currentDate:String = SimpleDateFormat("dd/MM/yyyy", Locale.ITALIAN).format(createOn.getTime());
        return currentDate + ":\n >> " + todo;
    }
}