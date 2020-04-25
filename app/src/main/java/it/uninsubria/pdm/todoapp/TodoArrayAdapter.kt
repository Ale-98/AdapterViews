package it.uninsubria.pdm.todoapp

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.max
import kotlin.math.min

class TodoArrayAdapter(private var activity: Activity, private var items: ArrayList<TodoItem>) : BaseAdapter(){

    private class ViewHolder(row: View?) {
        var msgTextView: TextView? = null
        var createdData: TextView? = null
        var imgTodo: ImageView? = null
        var deadlineDate: TextView? = null
        var circleProgressBar: CircularProgressBar? = null
        init {
            this.msgTextView = row?.findViewById(R.id.messageTextView)
            this.createdData = row?.findViewById(R.id.createdTextView)
            this.imgTodo = row?.findViewById(R.id.imageView)
            this.deadlineDate = row?.findViewById(R.id.deadlineTextView)
            this.circleProgressBar = row?.findViewById(R.id.circleProgressBar)
        }
    }
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View
        val viewHolder: ViewHolder
        if (convertView == null) {
            val inflater = activity?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = inflater.inflate(R.layout.list_item, null)
            viewHolder = ViewHolder(view)
            view.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as ViewHolder
        }
        var emp = items[position]
        viewHolder.msgTextView?.text = emp.todo
        viewHolder.createdData?.text = emp.dateToString(emp.createOn)
        viewHolder.deadlineDate?.text = emp.dateToString(emp.deadline)
        viewHolder.imgTodo?.setImageResource(R.mipmap.todo_listview_icon!!)
        val nowCal: Calendar = GregorianCalendar()
        val deltaMillis = nowCal.time.time - emp.createOn.time.time
        val intervalMillis = emp.deadline.time.time - emp.createOn.time.time
        if (intervalMillis < 0)
            viewHolder.circleProgressBar?.percentage = 1F
        else
            viewHolder.circleProgressBar?.percentage = max(0f, min(1f, deltaMillis / intervalMillis.toFloat()))
        return view
    }
    override fun getItem(i: Int): TodoItem {
        return items[i]
    }
    override fun getItemId(i: Int): Long {
        return i.toLong()
    }
    override fun getCount(): Int {
        return items.size
    }
}