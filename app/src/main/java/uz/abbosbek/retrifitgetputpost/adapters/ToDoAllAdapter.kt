package uz.abbosbek.retrifitgetputpost.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import uz.abbosbek.retrifitgetputpost.databinding.ItemRvBinding
import uz.abbosbek.retrifitgetputpost.models.MyToDo


class ToDoAllAdapter(var list: List<MyToDo> = emptyList(), val rvClick:RvClick) :
    RecyclerView.Adapter<ToDoAllAdapter.Vh>() {

    inner class Vh(val itemRvBinding: ItemRvBinding) : RecyclerView.ViewHolder(itemRvBinding.root) {

        @SuppressLint("NotifyDataSetChanged")
        fun onBind(myToDo: MyToDo, position: Int) {
            itemRvBinding.tvName.text = myToDo.sarlavha
            itemRvBinding.tvAbout.text = myToDo.matn
            itemRvBinding.tvHolati.text = myToDo.holat
            itemRvBinding.tvMuddat.text = myToDo.oxirgi_muddat

            itemRvBinding.imageMore.setOnClickListener {
                rvClick.menuClick(itemRvBinding.imageMore, myToDo)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(ItemRvBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(list[position], position)
    }

    interface RvClick{
        fun menuClick(imageView: ImageView, myToDo: MyToDo)
    }
}