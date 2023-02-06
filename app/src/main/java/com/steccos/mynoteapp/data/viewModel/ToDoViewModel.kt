package com.steccos.mynoteapp.data.viewModel

 import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.steccos.mynoteapp.data.ToDoDatabase
import com.steccos.mynoteapp.data.models.ToDoData
import com.steccos.mynoteapp.data.repository.ToDoRepository
 import dagger.hilt.android.AndroidEntryPoint
 import dagger.hilt.android.lifecycle.HiltViewModel
 import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


 class ToDoViewModel(application: Application) : AndroidViewModel(application){
// the symbol !! doesn't appear in the course
    private val toDoDao = ToDoDatabase.getDatabase(application).toDoDao()
    private var repository : ToDoRepository = ToDoRepository(toDoDao)

     //in next line if I removed =repository.getAllData as it didn't give me any error, and it didn't appear in lesson 22
     var getAllData: LiveData<List<ToDoData>>

    init {
        repository = ToDoRepository(toDoDao)
        getAllData = repository.getAllData
    }
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

}