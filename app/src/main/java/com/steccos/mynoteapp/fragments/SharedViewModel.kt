package com.steccos.mynoteapp.fragments

import android.app.Application
import android.view.View
import android.widget.AdapterView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.steccos.mynoteapp.R
import com.steccos.mynoteapp.data.models.Priority
import com.steccos.mynoteapp.data.models.ToDoData


class SharedViewModel(application: Application) : AndroidViewModel(application) {

     val emptyDatabase : MutableLiveData<Boolean> = MutableLiveData(false)

    fun checkIfDatabaseIsEmpty(toDoData: List<ToDoData>) {
        emptyDatabase.value = toDoData.isEmpty()
    }

    val listener : AdapterView.OnItemSelectedListener = object :
    AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(p0: AdapterView<*>?) {}
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            when (position) {
                0 -> { (parent?.getChildAt(0)as TextView).setTextColor(ContextCompat.getColor(application, R.color.red))}
                1 -> { (parent?.getChildAt(0)as TextView).setTextColor(ContextCompat.getColor(application, R.color.yellow))}
                2 -> { (parent?.getChildAt(0)as TextView).setTextColor(ContextCompat.getColor(application, R.color.purple_500))}
                3 -> { (parent?.getChildAt(0)as TextView).setTextColor(ContextCompat.getColor(application, R.color.light_blue))}
                4 -> { (parent?.getChildAt(0)as TextView).setTextColor(ContextCompat.getColor(application, R.color.green))}

            }
        }
    }

     fun verifyDataFromUser(title : String, description : String) : Boolean {
        return !(title.isEmpty() || description.isEmpty())
    }

     fun parsePriority(priority : String) : Priority {
        return when(priority){
            "Urgent" -> { Priority.URGENT}
            "High Priority" -> { Priority.HIGH}
            "Medium Priority" -> { Priority.MEDIUM}
            "Low Priority" -> { Priority.LOW}
            "Reminder" -> { Priority.REMINDER}
            else -> Priority.LOW
        }

    }
}