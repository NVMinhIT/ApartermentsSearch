package vnjp.monstarlaplifetime.apartmentssearch.screen.itemslist

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import vnjp.monstarlaplifetime.apartmentssearch.R
import vnjp.monstarlaplifetime.apartmentssearch.data.model.Room

class ItemsListAdapter(private val context: Context, private val onClickItem: (Int) -> Unit) :
    RecyclerView.Adapter<ItemsListAdapter.MyViewHolder>() {
    private var listRoom: List<Room> = emptyList()

    fun setListRoom(list: List<Room>) {
        listRoom = list
        notifyDataSetChanged()
    }

    fun getPosition(position: Int): Room {
        return listRoom.get(position)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ItemsListAdapter.MyViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.items_room, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listRoom.size
    }

    override fun onBindViewHolder(holder: ItemsListAdapter.MyViewHolder, position: Int) {
        val current = listRoom[position]
        holder.bind(current)
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvAccountRating: TextView = itemView.findViewById(R.id.tvAccountRating)
        private val tvAccountComment: TextView = itemView.findViewById(R.id.tvAccountComment)
        private val tvNameRoom: TextView = itemView.findViewById(R.id.tvNameRoom)
        private val tvPriceRoom: TextView = itemView.findViewById(R.id.tvPriceRoom)
        private val imgRoom: ImageView = itemView.findViewById(R.id.imgRoom)

        init {
            itemView.setOnClickListener {
                onClickItem(adapterPosition)
            }
        }

        @SuppressLint("CheckResult")
        fun bind(room: Room) {
            Glide.with(context)
                .load(R.drawable.room)
                .centerCrop()
                .into(imgRoom)
            tvAccountRating.text = room.accountRating.toString()
            tvAccountComment.text = room.accountComment.toString()
            tvNameRoom.text = room.nameRoom
            tvPriceRoom.text = room.priceRoom.toString()

        }
    }
}