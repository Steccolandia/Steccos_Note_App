package com.steccos.mynoteapp.data.viewModel

 import android.app.Application
 import androidx.lifecycle.AndroidViewModel
 import androidx.lifecycle.LiveData
 import androidx.lifecycle.viewModelScope
 import com.steccos.mynoteapp.data.ToDoDatabase
 import com.steccos.mynoteapp.data.models.ToDoData
 import com.steccos.mynoteapp.data.repository.ToDoRepository
 import kotlinx.coroutines.Dispatchers
 import kotlinx.coroutines.launch


class ToDoViewModel(application: Application) : AndroidViewModel(application){
//
    private val toDoDao = ToDoDatabase.getDatabase(application).toDoDao()
    private var repository : ToDoRepository = ToDoRepository(toDoDao)

     var getAllData: LiveData<List<ToDoData>> = repository.getAllData
     val sortByHighPriority : LiveData<List<ToDoData>> = repository.sortByHighPriority
     val sortByLowPriority : LiveData<List<ToDoData>> = repository.sortByLowPriority

    fun insertData(toDoData: ToDoData) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertData(toDoData)
        }
    }

    fun updateData(toDoData: ToDoData) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateData(toDoData)
        }
    }
     fun deleteItem(toDoData: ToDoData) {
         viewModelScope.launch(Dispatchers.IO) {
             repository.deleteItem(toDoData)
         }
     }

     fun deleteAll() {
         viewModelScope.launch(Dispatchers.IO) {
             repository.deleteAll()
         }
     }

     fun searchDatabase(searchQuery: String): LiveData<List<ToDoData>>{
         return repository.searchDatabase(searchQuery)
     }

}