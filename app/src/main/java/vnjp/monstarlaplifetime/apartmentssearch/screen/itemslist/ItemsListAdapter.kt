package vnjp.monstarlaplifetime.apartmentssearch.screen.itemslist

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import vnjp.monstarlaplifetime.apartmentssearch.R
import vnjp.monstarlaplifetime.apartmentssearch.data.model.Room
import vnjp.monstarlaplifetime.apartmentssearch.data.model.RoomTest
import vnjp.monstarlaplifetime.apartmentssearch.screen.detailroom.DetailRoomActivity


class ItemsListAdapter(private val context: Context) :
    RecyclerView.Adapter<ItemsListAdapter.MyViewHolder>() {
    private var listRoom: List<RoomTest> = emptyList()

    companion object {
        const val BUNDLE_ID_ROOM = "BUNDLE_ID_ROOM"
    }

    var isLike: Boolean = true

    fun setListRoom(list: List<RoomTest>) {
        listRoom = list
        notifyDataSetChanged()
    }

    fun getPosition(position: Int): RoomTest {
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
        private val imbLike: ImageButton = itemView.findViewById(R.id.imbLike)

//        init {
//            itemView.setOnClickListener {
//                onClickItem(adapterPosition)
//            }
//        }

        @SuppressLint("CheckResult")
        fun bind(room: RoomTest) {
            var requestOptions = RequestOptions()
            requestOptions = requestOptions.transform(CenterCrop(), RoundedCorners(22))
            Glide.with(context).load(R.drawable.room).apply(requestOptions).into(imgRoom)
//            Glide.with(context)
//                .load(R.drawable.room)
//                .apply(RequestOptions.bitmapTransform(RoundedCornersTransformation(context, 32, 0, RoundedCornersTransformation.CornerType.TOP_LEFT
//                )))
//                .into(imgRoom)

            tvAccountRating.text = room.accountRating.toString()
            tvAccountComment.text = room.accountComment.toString()
            tvNameRoom.text = room.nameRoom
            tvPriceRoom.text = room.priceRoom.toString()


            imgRoom.setOnClickListener {
                val intent = Intent(context, DetailRoomActivity::class.java)
                intent.putExtra(BUNDLE_ID_ROOM, room.idRoom)
                context.startActivity(intent)
            }
            imgRoom.setOnClickListener {
                setLikeRoom(isLike)
            }

        }

        fun setLikeRoom(like: Boolean) {
            if (!like) {
                imbLike.setImageDrawable(
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.ic_favorite
                    )
                )
                isLike = true

            } else {
                imbLike.setImageDrawable(
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.ic_favorite_hint
                    )
                )
                isLike = false


            }
        }

        @SuppressLint("CheckResult")
        fun bind(room: Room) {
            Glide.with(context)
                .load(room.image)
                .centerCrop()
                .into(imgRoom)
            tvAccountRating.text = room.comments?.size.toString()
            tvAccountComment.text = room.comments?.size.toString()
            tvNameRoom.text = room.name
            tvPriceRoom.text = room.price.toString()


        }
    }
}