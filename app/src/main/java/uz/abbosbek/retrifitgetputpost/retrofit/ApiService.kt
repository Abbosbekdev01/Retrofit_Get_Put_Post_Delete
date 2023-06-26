package uz.abbosbek.retrifitgetputpost.retrofit

import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import uz.abbosbek.retrifitgetputpost.models.MyPostToDoRequest
import uz.abbosbek.retrifitgetputpost.models.MyPostToDoResponse
import uz.abbosbek.retrifitgetputpost.models.MyToDo

interface ApiService {

    @GET("plan")
    suspend fun getAllToDo():List<MyToDo>

    @POST("plan/")
    suspend fun addToDo(@Body myPostToDoRequest: MyPostToDoRequest):MyPostToDoResponse

    @PUT("plan/{id}/")
    suspend fun updateToDo(@Path("id") id:Int, @Body myPostToDoRequest: MyPostToDoRequest):MyPostToDoResponse

    @DELETE("plan/{id}/")
    suspend fun deleteToDo(@Path ("id") id: Int):Int
}