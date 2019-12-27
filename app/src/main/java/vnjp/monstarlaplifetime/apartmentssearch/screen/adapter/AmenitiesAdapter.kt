package vnjp.monstarlaplifetime.apartmentssearch.screen.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import vnjp.monstarlaplifetime.apartmentssearch.R
import vnjp.monstarlaplifetime.apartmentssearch.data.model.Amenities

class AmenitiesAdapter(private val context: Context) :
    RecyclerView.Adapter<AmenitiesAdapter.MyViewHolder>() {

    private var listAmenities: List<Amenities> = emptyList()
    fun setListAmenities(list: List<Amenities>) {
        listAmenities = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AmenitiesAdapter.MyViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.items_amenities, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listAmenities.size
    }

    override fun onBindViewHolder(holder: AmenitiesAdapter.MyViewHolder, position: Int) {
        val current = listAmenities[position]
        holder.bind(current)
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imgIcon: ImageView = itemView.findViewById(R.id.imgIcon)
        private val tvNameAmenities: TextView = itemView.findViewById(R.id.tvNameAmenities)

        fun bind(amenities: Amenities) {
            Glide.with(context)
                .load(amenities.imageAmenities)
                .centerCrop()
                .into(imgIcon)
            tvNameAmenities.text = amenities.nameAmenities
        }
    }
}