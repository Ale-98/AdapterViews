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
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    // Idetificativo usato per una subactivity
    val NEW_ITEM_REQUEST = 1
    // If true data are represented in list mode else in grid mode
    val list_mode = true
    // the array list containing the Todo items
    val todoItems = ArrayList<TodoItem>()
    // ArrayAdapter converts an ArrayList of Todo items into View items
    lateinit var adapter:TodoArrayAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(list_mode) setContentView(R.layout.activity_main)
        else setContentView(R.layout.grid_activity_main)

        // Aggiunge la toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setLogo(R.mipmap.ic_launcher)
        supportActionBar?.setDisplayUseLogoEnabled(true)

        // Crea DBAdapter e aggiunge tutti gli elementi del DB all'ArrayList di TodoItems
        val dbAdapter = DBAdapter(this)
        todoItems.addAll(dbAdapter.getAllItems())

        // Per implementare l'eleiminazione di un item tenendoci premuto sopra
        list_view.setOnItemLongClickListener{parent, view, position, id ->
            onLongClickDeleteItems(position)
            true
        }

        // Crea il custom adapter passandogli la lista di items da mettere nella listView
        adapter = TodoArrayAdapter(this, todoItems)
        // Assegna l'adapter alla listView
        if(list_mode) list_view.adapter = adapter
        else grid_view.adapter = adapter
    }

    // Richiesto per aggiungere menu alla ToolBar
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_activity_main, menu)
        return true
    }

    // Per aggiungere un'azione alla pressione delle icone del menu
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
            // Creazione intent per far partire subActivity
            val i = Intent(applicationContext, AddNewItemActivity::class.java)
            startActivityForResult(i, NEW_ITEM_REQUEST)
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    // Handler innescato al termine della subActivity
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // Check which request we're responding to
        if (requestCode == NEW_ITEM_REQUEST) {
        // Make sure the request was successful
            if (resultCode == Activity.RESULT_OK) {
                // Estrae i dati contenuti nell'intent ritornato dalla subActivity al clic della back-Arrow
                val returnValue = data?.getStringExtra("TODO_TASK")
                val deadLine = data?.getStringExtra("TODO_DEADLINE")
                val imgUrl = data?.getStringExtra("TODO_IMAGE_URL")
                val df = SimpleDateFormat("dd MM yyyy")
                val date = df.parse(deadLine)
                val cal = GregorianCalendar()
                cal.setTime(date)

                Log.d(MainActivity::class.java.name, "onActivityResult() -> $returnValue")
                if (returnValue != null) {
                    // Chiama funzione per aggiungere nuovo item al DB e alla listView tramite l'arrayList e l'adapter
                    addNewItem(returnValue, cal, imgUrl)
                }
                return
            }
        }
    }

    fun addNewItem(text:String, deadLine:GregorianCalendar, imgUrl:String?){
        if(text == null || text.equals("")){
            Toast.makeText(applicationContext, "Empty Todo String", Toast.LENGTH_LONG).show()
            return
        }
        // Crea nuovo todoItem e lo aggiunge al DB
        val newTodo = TodoItem(text)
        newTodo.deadline = deadLine
        if(imgUrl!=null && !imgUrl.isEmpty()){
            newTodo.imgUrl = imgUrl
        }
        val dbAdapter = DBAdapter(this)
        dbAdapter.insertItem(newTodo)

        // Lo aggiunge alche all'arrayList e notifica l'arrayAdapter
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
