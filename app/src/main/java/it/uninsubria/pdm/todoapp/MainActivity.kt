package it.uninsubria.pdm.todoapp

import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    // the array list containing the Todo items
    val todoItems = ArrayList<TodoItem>()
    // ArrayAdapter converts an ArrayList of Todo items into View items
    var adapter:ArrayAdapter<TodoItem>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, todoItems)
        // View items are loaded into the ListView container.
        list_view.adapter = adapter
    }

    // On click for button
    fun addItemToList(view: View){
        val text:String = editText.text.toString()
        if(text == null || text.equals("")){
            Toast.makeText(applicationContext, "Empty Todo String", Toast.LENGTH_LONG).show()
            return
        }
        todoItems.add(0, TodoItem(text))
        adapter?.notifyDataSetChanged()
        editText.setText("")
        val inputManager: InputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(editText.applicationWindowToken, 0)
        editText.clearFocus()
    }
}
