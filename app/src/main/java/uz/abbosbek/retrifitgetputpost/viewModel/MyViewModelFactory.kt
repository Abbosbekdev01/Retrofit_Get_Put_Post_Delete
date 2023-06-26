package uz.abbosbek.retrifitgetputpost.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import uz.abbosbek.retrifitgetputpost.repository.ToDoRepository
import java.lang.IllegalArgumentException

class MyViewModelFactory(val toDoRepository: ToDoRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(ToDoViewModel::class.java)){
            return ToDoViewModel(toDoRepository) as T
        }
        throw IllegalArgumentException("Error MyViewModelFactory")
    }
}