package it.uninsubria.pdm.todoapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.grid_activity_main.*
import kotlinx.android.synthetic.main.tool_bar.*

class MainActivity : AppCompatActivity() {

    val NEW_ITEM_REQUEST = 1
    // If true data are represented in list mode else in grid mode
    val list_mode = true
    // the array list containing the Todo items
    val todoItems = ArrayList<TodoItem>()
    // ArrayAdapter converts an ArrayList of Todo items into View items
    lateinit var adapter:ArrayAdapter<TodoItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(list_mode) setContentView(R.layout.activity_main)
        else setContentView(R.layout.grid_activity_main)

        setSupportActionBar(toolbar)
        supportActionBar?.setLogo(R.mipmap.ic_launcher)
        supportActionBar?.setDisplayUseLogoEnabled(true)

        val dbAdapter = DBAdapter(this)
        todoItems.addAll(dbAdapter.getAllItems())

        list_view.setOnItemLongClickListener{parent, view, position, id ->
            onLongClickDeleteItems(position)
            true
        }

        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, todoItems)
        // View items are loaded into the ListView container.
        if(list_mode) list_view.adapter = adapter
        else grid_view.adapter = adapter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_activity_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id: Int = item.getItemId()
        if (id == R.id.action_settings) {
            Toast.makeText(
                applicationContext,
                "Not yet implemented!",
                Toast.LENGTH_SHORT
            ).show()
            return true
        } else if (id == R.id.action_new_item) {
            val i = Intent(applicationContext, AddNewItemActivity::class.java)
            startActivityForResult(i, NEW_ITEM_REQUEST)
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // Check which request we're responding to
        if (requestCode == NEW_ITEM_REQUEST) {
        // Make sure the request was successful
            if (resultCode == Activity.RESULT_OK) {
                val returnValue = data?.getStringExtra("TODO_TASK")
                Log.d(MainActivity::class.java.name, "onActivityResult() -> $returnValue")
                if (returnValue != null) {
                    addNewItem(returnValue)
                }
                return
            }
        }
    }

    fun addNewItem(text:String){
        if(text == null || text.equals("")){
            Toast.makeText(applicationContext, "Empty Todo String", Toast.LENGTH_LONG).show()
            return
        }
        val newTodo = TodoItem(text)
        val dbAdapter = DBAdapter(this)
        dbAdapter.insertItem(newTodo)

        todoItems.add(0, newTodo)
        adapter.notifyDataSetChanged()
        val inputManager: InputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        //inputManager.hideSoftInputFromWindow(editText.applicationWindowToken, 0)
        //editText.clearFocus()
    }

    // On long click for deleting todos
    fun onLongClickDeleteItems(position:Int){
        val item = list_view.getItemAtPosition(position) as TodoItem
        Toast.makeText(applicationContext, "Deleted item: $item", Toast.LENGTH_LONG).show()
        val dbAdapter = DBAdapter(this)
        dbAdapter.deleteItem(item)
        todoItems.removeAt(position)
        adapter.notifyDataSetChanged()
    }
}
