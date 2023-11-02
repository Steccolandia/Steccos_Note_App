package com.steccos.mynoteapp.fragments

import android.os.Build
import android.view.View
import android.widget.Spinner
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.navigation.findNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.steccos.mynoteapp.R
import com.steccos.mynoteapp.data.models.Background
import com.steccos.mynoteapp.data.models.Priority
import com.steccos.mynoteapp.data.models.ToDoData
import com.steccos.mynoteapp.fragments.list.ListFragmentDirections

    class BindingAdapters {

        companion object {
            @BindingAdapter("android:navigateToAddFragment")
            @JvmStatic
            fun navigateToAddFragment(view: FloatingActionButton, navigate : Boolean) {
                view.setOnClickListener{
                    if(navigate){
                        view.findNavController().navigate(R.id.action_listFragment_to_addFragment)
                    }
                }
            }
            @BindingAdapter("android:emptyDatabase")
            @JvmStatic
            fun emptyDatabase(view : View, emptyDatabase : MutableLiveData<Boolean>) {
                when(emptyDatabase.value) {
                    true -> view.visibility = View.VISIBLE
                    false -> view.visibility = View.INVISIBLE
                    else -> {}
                }
            }
            @BindingAdapter("android:parsePriorityToInt")
            @JvmStatic
            fun parsePriorityToInt(view : Spinner, priority: Priority) {
                when(priority){
                    Priority.URGENT -> { view.setSelection(0) }
                    Priority.HIGH -> { view.setSelection(1) }
                    Priority.MEDIUM -> { view.setSelection(2) }
                    Priority.LOW -> { view.setSelection(3) }
                    Priority.REMINDER -> { view.setSelection(4) }

                }

            }
            @RequiresApi(Build.VERSION_CODES.M)
            @BindingAdapter("android:parsePriorityColor")
            @JvmStatic
            fun parsePriorityColor(cardView: CardView, priority: Priority) {
                when(priority) {
                    Priority.URGENT -> {cardView.setCardBackgroundColor(cardView.context.getColor(R.color.red))}
                    Priority.HIGH -> {cardView.setCardBackgroundColor(cardView.context.getColor(R.color.yellow))}
                    Priority.MEDIUM -> {cardView.setCardBackgroundColor(cardView.context.getColor(R.color.purple_500))}
                    Priority.LOW -> {cardView.setCardBackgroundColor(cardView.context.getColor(R.color.light_blue))}
                    Priority.REMINDER -> {cardView.setCardBackgroundColor(cardView.context.getColor(R.color.green))}
                }
            }
            @BindingAdapter("android:sendDataToUpdateFragment")
            @JvmStatic
            fun sendDataToUpdateFragment(view : ConstraintLayout, currentItem : ToDoData){
                view.setOnClickListener {
                    val action = ListFragmentDirections.actionListFragmentToUpdateFragment(currentItem)
                    view.findNavController().navigate(action)
                }
            }
        }
    }
