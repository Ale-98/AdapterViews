package it.uninsubria.pdm.todoapp

// Classe che descrive la struttura del DB
class DBContract {
    companion object{
        val DATABASE_VERSION = 3 // Per capire se deve essere eseguito onUpgrade o onDowngrade in base alla varsione del db
        val DATABASE_NAME = "TodoList.db" // Tutto il db starà all'interno di un file con questo nome
    }

    abstract class TodoTable{
        companion object{
            val TABLE_NAME = "TodoItems"
            val KEY_ID = "id"
            val KEY_TASK = "task"
            val KEY_DATE = "creation_date"
            val KEY_DEADLINE = "deadline_date"
            val KEY_IMAGE_URL = "image_url"
            val COLUMNS  = arrayOf(KEY_ID, KEY_TASK, KEY_DATE, KEY_DEADLINE, KEY_IMAGE_URL)
        }
    }
}