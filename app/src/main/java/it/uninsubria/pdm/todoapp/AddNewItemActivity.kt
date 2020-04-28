package it.uninsubria.pdm.todoapp

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_add_new_item.*
import kotlinx.android.synthetic.main.tool_bar.*
import java.util.*

class AddNewItemActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_item)

        // Settaggi per includere la toolbar(messa in un XML a parte e inclusa nei layout usando tag 'include'
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = "Add new ToDo Item"

        // Per dare focus alla textView per inserire la descrizione per il nuovo todo
        editNewTodo.requestFocus()
        // Per aggiungere il date picker
        addDeadLineTextView.setOnClickListener {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)
            val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener {
                    view, year, monthOfYear, dayOfMonth ->
                // Display Selected date in textbox
                addDeadLineTextView.text = ("" + dayOfMonth + " " + (monthOfYear+1) + " " + year)
            }, year, month, day)
            dpd.show()
        }
    }

    // Per abilitare la back-arrow
    // Crea intent in cui inserisce i dati da ritornare alla main activity e poi lo passa come argomento a setResult(...)
    override fun onSupportNavigateUp(): Boolean {
        val resultIntent = Intent()
        resultIntent.putExtra("TODO_TASK", editNewTodo.text.toString())
        resultIntent.putExtra("TODO_DEADLINE", addDeadLineTextView.text.toString())
        resultIntent.putExtra("TODO_IMAGE_URL", imgUrlEditText.text.toString())
        Log.d("NewTodoActivity", "onSupportNavigateUp() -> " + editNewTodo.text)
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
        return true
    }
}
