package uz.abbosbek.mytelegram.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView
import uz.abbosbek.mytelegram.databinding.ItemRvBinding
import uz.abbosbek.mytelegram.models.Users
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

class RvAdapter(val list: ArrayList<Users>, val context: Context, val rvClick: RvClick) :
    RecyclerView.Adapter<RvAdapter.Vh>() {

    inner class Vh(private val itemRvBinding: ItemRvBinding) :
        RecyclerView.ViewHolder(itemRvBinding.root) {
        fun onBind(users: Users, position: Int) {

            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH) + 1 // Note: MONTH is zero-based.
            val day = calendar.get(Calendar.DAY_OF_MONTH)
            val data = "$day/$month/$year" //15/6/2023

            //todo: bu yerda ma'lumot saqlangan kuni soat ko'rinib turadi keyingi kunlari sanasi bilan ko'rinadi
            itemRvBinding.itemDateTime.isVisible = !data.contains(users.data.toString())
//             if (data.contains(users.data.toString())){
//                itemRvBinding.itemDateTime.isVisible = false
//            }else{
//                itemRvBinding.itemDateTime.isVisible = true
//            }

            itemRvBinding.tvName.text = users.name
            itemRvBinding.tvAbout.text = users.about
            itemRvBinding.itemTime.text = users.time
            itemRvBinding.itemDateTime.text = users.data

            Glide.with(itemView.context).load(users.photoPath).into(itemRvBinding.imageUser)


            itemRvBinding.itemMenu.setOnClickListener {
                rvClick.itemPopupMenu(users, itemRvBinding.itemMenu, position)
                notifyItemChanged(position)
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
}

interface RvClick {
    fun itemPopupMenu(users: Users, imageView: ImageView, position: Int)

}