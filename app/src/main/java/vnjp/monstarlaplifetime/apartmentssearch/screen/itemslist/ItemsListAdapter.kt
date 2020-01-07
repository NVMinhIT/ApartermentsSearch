package vnjp.monstarlaplifetime.apartmentssearch.screen.itemslist

import android.annotation.SuppressLint
import android.content.Context
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
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import vnjp.monstarlaplifetime.apartmentssearch.R
import vnjp.monstarlaplifetime.apartmentssearch.data.model.Room


class ItemsListAdapter(var query: FirebaseRecyclerOptions<Room>, val context: Context) :
    FirebaseRecyclerAdapter<Room, ItemsListAdapter.MyViewHolder>(query) {

    var onClick: ((Room, String) -> Unit)? = null

    companion object {
        const val BUNDLE_ID_ROOM = "BUNDLE_ID_ROOM"
    }

    var isLike: Boolean = true

    override fun onBindViewHolder(p0: MyViewHolder, p1: Int, p2: Room) {
        p0.bind(p2)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ItemsListAdapter.MyViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.items_room, parent, false)
        return MyViewHolder(view)
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvAccountRating: TextView = itemView.findViewById(R.id.tvAccountRating)
        private val tvAccountComment: TextView = itemView.findViewById(R.id.tvAccountComment)
        private val tvNameRoom: TextView = itemView.findViewById(R.id.tvNameRoom)
        private val tvPriceRoom: TextView = itemView.findViewById(R.id.tvPriceRoom)
        private val imgRoom: ImageView = itemView.findViewById(R.id.imgRoom)
        private val imbLike: ImageButton = itemView.findViewById(R.id.imbLike)

        @SuppressLint("CheckResult")
        fun bind(room: Room) {
            var requestOptions = RequestOptions()
            requestOptions = requestOptions.transform(CenterCrop(), RoundedCorners(22))
            Glide.with(context).load(room.image).apply(requestOptions).into(imgRoom)
            tvAccountComment.text = room.comments?.size.toString()
            tvAccountRating.text = room.comments?.size.toString()
            tvPriceRoom.text = room.price.toString()
            tvNameRoom.text = room.name
            imgRoom.setOnClickListener {
                snapshots.getSnapshot(adapterPosition).key?.let { key ->
                    onClick?.invoke(room, key)

                }
            }

            imbLike.setOnClickListener {
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

    }


}