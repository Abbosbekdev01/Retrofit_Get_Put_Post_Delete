package uz.abbosbek.retrifitgetputpost.repository

import uz.abbosbek.retrifitgetputpost.models.MyPostToDoRequest
import uz.abbosbek.retrifitgetputpost.retrofit.ApiService

class ToDoRepository(private val apiService: ApiService) {

    suspend fun getAllToDo() = apiService.getAllToDo()
    suspend fun addToDo(myPostToDoRequest: MyPostToDoRequest) = apiService.addToDo(myPostToDoRequest)
    suspend fun updateToDo(id:Int, myPostToDoRequest: MyPostToDoRequest) = apiService.updateToDo(id, myPostToDoRequest)
    suspend fun deleteToDo(id: Int) = apiService.deleteToDo(id)

}