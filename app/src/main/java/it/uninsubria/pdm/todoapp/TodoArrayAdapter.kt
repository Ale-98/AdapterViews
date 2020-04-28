package it.uninsubria.pdm.todoapp

import android.app.Activity
import android.content.Context
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import java.io.IOException
import java.net.URL
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.max
import kotlin.math.min

// Custom adapter per la listView
class TodoArrayAdapter(private var activity: Activity, private var items: ArrayList<TodoItem>) : BaseAdapter(){

    // Classe holder per contenere tutti gli elementi grafici(estratti quindi da file xml del custom item
    // usato per popolare la listView
    private class ViewHolder(row: View?) {
        var msgTextView: TextView? = null
        var createdData: TextView? = null
        var imgTodo: ImageView? = null
        var deadlineDate: TextView? = null
        var circleProgressBar: CircularProgressBar? = null
        var imgTextView: TextView? = null
        init {
            this.msgTextView = row?.findViewById(R.id.messageTextView)
            this.createdData = row?.findViewById(R.id.createdTextView)
            this.imgTodo = row?.findViewById(R.id.imageView)
            this.deadlineDate = row?.findViewById(R.id.deadlineTextView)
            this.circleProgressBar = row?.findViewById(R.id.circleProgressBar)
            this.imgTextView = row?.findViewById(R.id.imgUrlEditText)
        }
    }

    // Richiesto dalla classe estesa BaseAdapter per implementare customAdapter
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View
        val viewHolder: ViewHolder
        // Implementazione meccanismo di riciclo delle view
        if (convertView == null) {
            // crea una nuova view caricandole il layout e assegnandole un nuovo viewHolder
            val inflater = activity?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = inflater.inflate(R.layout.list_item, null)
            viewHolder = ViewHolder(view)
            view.tag = viewHolder
        } else {
            // Altrimenti riutilizza la convertView e assegna il suo tag al viewHolder
            view = convertView
            viewHolder = view.tag as ViewHolder
        }
        // Considera l'item che si vuole aggiungere alla lista nella posizione data da parametro di getView
        // quello per il quele prima è stata recuperata una view o riciclandola o creandone una nuova
        var emp = items[position]
        // Assegna al viewHolder le proprietà del nuovo item
        viewHolder.msgTextView?.text = emp.todo
        viewHolder.createdData?.text = emp.dateToString(emp.createOn)
        viewHolder.deadlineDate?.text = emp.dateToString(emp.deadline)
        val nowCal: Calendar = GregorianCalendar()
        // per circularProgressBar
        val deltaMillis = nowCal.time.time - emp.createOn.time.time
        val intervalMillis = emp.deadline.time.time - emp.createOn.time.time
        if (intervalMillis < 0)
            viewHolder.circleProgressBar?.percentage = 1F
        else
            viewHolder.circleProgressBar?.percentage = max(0f, min(1f, deltaMillis / intervalMillis.toFloat()))

        // Imposto inizialmente l'immagine di default
        viewHolder.imgTodo?.setImageResource(R.mipmap.todo_listview_icon!!)
        val urlImage = viewHolder.imgTextView?.text?.trim().toString()
        if (urlImage != "") {
            // Se l'immagine esiste in Internet la scarica con un thread
            Thread(Runnable {
                try {
                    val url = URL(urlImage)
                    val bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream())
                    activity.runOnUiThread(Runnable {
                        viewHolder.imgTodo?.setImageBitmap(bmp)
                    })
                }
                catch (e: IOException) {
                    Log.w("TodoArrayAdapter", "Error in downloading image from URL " + urlImage)
                }
            }).start()
        }
        return view
    }

    // Per ottenere l'i-esimo item
    override fun getItem(i: Int): TodoItem {
        return items[i]
    }

    // Non implementato
    override fun getItemId(i: Int): Long {
        return i.toLong()
    }

    // Per ottenere il numero di elementi nell'arrayList
    override fun getCount(): Int {
        return items.size
    }
}