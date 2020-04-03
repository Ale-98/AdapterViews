package it.uninsubria.pdm.todoapp

import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isInvisible
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.editText
import kotlinx.android.synthetic.main.grid_activity_main.*

class MainActivity : AppCompatActivity() {
    // If true data are represented in list mode else in grid mode
    val list_mode = true
    // the array list containing the Todo items
    val todoItems = ArrayList<TodoItem>()
    // ArrayAdapter converts an ArrayList of Todo items into View items
    var adapter:ArrayAdapter<TodoItem>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(list_mode) setContentView(R.layout.activity_main)
        else setContentView(R.layout.grid_activity_main)

        val helper = DBHelper.getInstance(this)
        todoItems.addAll(helper.getAllItems())

        list_view.setOnItemLongClickListener{parent, view, position, id ->
            deleteItems(position)
            true
        }

        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, todoItems)
        // View items are loaded into the ListView container.
        if(list_mode) list_view.adapter = adapter
        else grid_view.adapter = adapter
    }

    // On click for button
    fun addItemToList(view: View){
        val text:String = editText.text.toString()
        if(text == null || text.equals("")){
            Toast.makeText(applicationContext, "Empty Todo String", Toast.LENGTH_LONG).show()
            return
        }
        val newTodo = TodoItem(text)
        val helper = DBHelper.getInstance(this)
        helper.insertItem(newTodo)

        todoItems.add(0, newTodo)
        adapter?.notifyDataSetChanged()
        editText.setText("")
        val inputManager: InputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(editText.applicationWindowToken, 0)
        editText.clearFocus()
    }

    // On long click for deleting todos
    fun deleteItems(position:Int){
        val item = list_view.getItemAtPosition(position) as TodoItem
        Toast.makeText(applicationContext, "Deleted item: $item", Toast.LENGTH_LONG).show()
        val helper = DBHelper.getInstance(this)
        helper.deleteItem(item)
        todoItems.removeAt(position)
        adapter?.notifyDataSetChanged()
    }
}
