package it.uninsubria.pdm.todoapp

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_add_new_item.*
import kotlinx.android.synthetic.main.tool_bar.*

class AddNewItemActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_item)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = "Add new ToDo Item"
    }

    override fun onSupportNavigateUp(): Boolean {
        val resultIntent = Intent()
        resultIntent.putExtra("TODO_TASK", editNewTodo.text.toString())
        Log.d("NewTodoActivity", "onSupportNavigateUp() -> " + editNewTodo.text)
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
        return true
    }
}
