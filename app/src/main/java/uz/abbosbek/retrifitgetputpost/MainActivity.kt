package uz.abbosbek.retrifitgetputpost

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import uz.abbosbek.retrifitgetputpost.adapters.ToDoAllAdapter
import uz.abbosbek.retrifitgetputpost.databinding.ActivityMainBinding
import uz.abbosbek.retrifitgetputpost.databinding.ItemDialogBinding
import uz.abbosbek.retrifitgetputpost.models.MyPostToDoRequest
import uz.abbosbek.retrifitgetputpost.models.MyToDo
import uz.abbosbek.retrifitgetputpost.repository.ToDoRepository
import uz.abbosbek.retrifitgetputpost.retrofit.ApiClient
import uz.abbosbek.retrifitgetputpost.utils.Status
import uz.abbosbek.retrifitgetputpost.viewModel.MyViewModelFactory
import uz.abbosbek.retrifitgetputpost.viewModel.ToDoViewModel

class MainActivity : AppCompatActivity(), ToDoAllAdapter.RvClick {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private lateinit var toDoViewModel: ToDoViewModel
    private val toDoRepository by lazy { ToDoRepository(ApiClient.getApiService()) }
    private val toDoAllAdapter by lazy { ToDoAllAdapter(rvClick = this) }

//    private var dialog: AlertDialog.Builder? = null
//    private var itemDialog: ItemDialogBinding? = null

    private val TAG = "MainActivity"

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        toDoViewModel = ViewModelProvider(
            this,
            MyViewModelFactory(toDoRepository)
        ).get(ToDoViewModel::class.java)

        binding.rv.adapter = toDoAllAdapter

        toDoViewModel.getAllToDo()

            .observe(this) {
                when (it.status) {
                    Status.LOADING -> {
                        Log.d(TAG, "onCreate: Loading")
                        binding.progressBar.visibility = View.VISIBLE
                    }

                    Status.ERROR -> {
                        Log.d(TAG, "onCreate: Error ${it.message}")
                        binding.progressBar.visibility = View.INVISIBLE
                        Toast.makeText(this, "Error ${it.message}", Toast.LENGTH_SHORT).show()
                    }

                    Status.SUCCESS -> {
                        Log.d(TAG, "onCreate: Success ${it.data}")
                        toDoAllAdapter.list = it.data!!
                        toDoAllAdapter.notifyDataSetChanged()
                        binding.progressBar.visibility = View.INVISIBLE
                    }
                }
            }

        binding.btnAdd.setOnClickListener {
            val dialog = AlertDialog.Builder(this).create()
            val itemDialog = ItemDialogBinding.inflate(layoutInflater)
            dialog.setView(itemDialog.root)
            itemDialog.apply {
                btnSave.setOnClickListener {

                    val myPostToDoRequest = MyPostToDoRequest(
                        spinnerStatus.selectedItem.toString(),
                        edtAbout.text.toString().trim(),
                        edtDeadline.text.toString().trim(),
                        edtTitle.text.toString().trim()
                    )
                    toDoViewModel.addMyToDo(myPostToDoRequest).observe(this@MainActivity) {
                        when (it.status) {
                            Status.LOADING -> {
                                progressPost.visibility = View.VISIBLE
                                linearDialog.isEnabled = false
                            }

                            Status.ERROR -> {
                                Toast.makeText(
                                    this@MainActivity,
                                    "${it.message}",
                                    Toast.LENGTH_SHORT
                                ).show()

                                progressPost.visibility = View.INVISIBLE
                                linearDialog.isEnabled = true
                            }

                            Status.SUCCESS -> {
                                Toast.makeText(
                                    this@MainActivity,
                                    "${it.data?.holat}-> ${it.data?.id}",
                                    Toast.LENGTH_SHORT
                                ).show()
                                dialog.cancel()
                            }
                        }
                    }
                }
            }
            dialog.show()
        }
    }

    override fun menuClick(imageView: ImageView, myToDo: MyToDo) {
        val popupMenu = PopupMenu(this, imageView)
        popupMenu.inflate(R.menu.todo_menu)

        popupMenu.setOnMenuItemClickListener {

            when (it.itemId) {
                R.id.menu_edit -> {
                    editToDo(myToDo)
                }

                R.id.menu_delete -> {
                    deleteToDo(myToDo)
                }
            }

            true
        }
        popupMenu.show()
    }

    private fun editToDo(myToDo: MyToDo) {
        val dialog = AlertDialog.Builder(this).create()
        val itemDialog = ItemDialogBinding.inflate(layoutInflater)
        dialog.setView(itemDialog.root)
        itemDialog.apply {
            edtAbout.setText(myToDo.matn)
            edtTitle.setText(myToDo.sarlavha)
            edtDeadline.setText(myToDo.oxirgi_muddat)

            when (myToDo.holat) {
                "Yangi" -> spinnerStatus.setSelection(0)
                "Bajarilmoqda" -> spinnerStatus.setSelection(1)
                "Tugallandi" -> spinnerStatus.setSelection(2)
            }

            btnSave.setOnClickListener {
                val myPostToDoRequest = MyPostToDoRequest(
                    spinnerStatus.selectedItem.toString(),
                    edtAbout.text.toString().trim(),
                    edtDeadline.toString().trim(),
                    edtTitle.text.toString().trim()
                )

                toDoViewModel.updateMyToDo(myToDo.id, myPostToDoRequest)
                    .observe(this@MainActivity) {
                        when (it.status) {
                            Status.LOADING -> {
                                progressPost.visibility = View.VISIBLE
                                linearDialog.isEnabled = false
                            }

                            Status.ERROR -> {
                                progressPost.visibility = View.VISIBLE
                                linearDialog.isEnabled = true
                                Toast.makeText(
                                    this@MainActivity,
                                    "ERROR ${it.message}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                            Status.SUCCESS -> {
                                Toast.makeText(
                                    this@MainActivity,
                                    "${it.data?.sarlavha} ${it.data?.id} with saved",
                                    Toast.LENGTH_SHORT
                                ).show()
                                dialog.cancel()
                            }
                        }
                    }
            }
        }
        dialog.show()
    }

    private fun deleteToDo(myToDo: MyToDo){
        toDoViewModel.deleteToDo(myToDo.id).observe(this){

        }
    }
}