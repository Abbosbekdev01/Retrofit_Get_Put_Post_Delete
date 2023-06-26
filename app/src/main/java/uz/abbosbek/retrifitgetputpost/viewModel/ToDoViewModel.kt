package uz.abbosbek.retrifitgetputpost.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import uz.abbosbek.retrifitgetputpost.models.MyPostToDoRequest
import uz.abbosbek.retrifitgetputpost.models.MyPostToDoResponse
import uz.abbosbek.retrifitgetputpost.models.MyToDo
import uz.abbosbek.retrifitgetputpost.repository.ToDoRepository
import uz.abbosbek.retrifitgetputpost.retrofit.ApiClient
import uz.abbosbek.retrifitgetputpost.utils.Resource
import uz.abbosbek.retrifitgetputpost.utils.Status
import java.lang.Exception

class ToDoViewModel(val toDoRepository: ToDoRepository) : ViewModel() {

    private val liveData = MutableLiveData<Resource<List<MyToDo>>>()


    fun getAllToDo(): MutableLiveData<Resource<List<MyToDo>>> {
        viewModelScope.launch {
            liveData.postValue(Resource.loading("loading"))

            try {

                coroutineScope {
                    val list = async {
                        toDoRepository.getAllToDo()
                    }.await()

                    liveData.postValue(Resource.success(list))
                }

            } catch (e: Exception) {
                liveData.postValue(Resource.error(e.message))
            }
        }
        return liveData
    }

    private val postLiveData = MutableLiveData<Resource<MyPostToDoResponse>>()

    fun addMyToDo(myPostToDoRequest: MyPostToDoRequest): MutableLiveData<Resource<MyPostToDoResponse>> {
        viewModelScope.launch {
            postLiveData.postValue(Resource.loading("Loading post "))
            try {
                coroutineScope {
                    val response = async {
                        toDoRepository.addToDo(myPostToDoRequest)
                    }.await()
                    postLiveData.postValue(Resource.success(response))
                    getAllToDo()
                }
            } catch (e: Exception) {
                postLiveData.postValue(Resource.error(e.message))
            }
        }
        return postLiveData
    }

    private val liveDataUpdate = MutableLiveData<Resource<MyPostToDoResponse>>()
    fun updateMyToDo(id:Int, myPostToDoRequest: MyPostToDoRequest):MutableLiveData<Resource<MyPostToDoResponse>>{

        viewModelScope.launch {
            liveDataUpdate.postValue(Resource.loading("Loading update "))
            try {
                coroutineScope {
                    val response = async {
                        toDoRepository.updateToDo(id,myPostToDoRequest)
                    }.await()
                    liveDataUpdate.postValue(Resource.success(response))
                    getAllToDo()
                }
            } catch (e: Exception) {
                liveDataUpdate.postValue(Resource.error(e.message))
            }
        }

        return liveDataUpdate
    }

    private val deleteLiveData = MutableLiveData<Resource<Int>>()
    fun deleteToDo(id: Int): MutableLiveData<Resource<Int>>{
        viewModelScope.launch {
            deleteLiveData.postValue(Resource.loading("Loading delete "))
            try {
                coroutineScope {
                    val response = async {
                        toDoRepository.deleteToDo(id)
                    }.await()
                    deleteLiveData.postValue(Resource.success(response))
                    getAllToDo()
                }
            } catch (e: Exception) {
                deleteLiveData.postValue(Resource.error(e.message))
            }
        }

        return deleteLiveData
    }
}
