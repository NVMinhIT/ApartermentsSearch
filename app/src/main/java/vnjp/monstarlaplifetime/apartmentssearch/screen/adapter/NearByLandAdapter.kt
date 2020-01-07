package vnjp.monstarlaplifetime.apartmentssearch.screen.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import vnjp.monstarlaplifetime.apartmentssearch.R
import vnjp.monstarlaplifetime.apartmentssearch.data.model.NearbyLandmark

class NearByLandAdapter(private val context: Context) :
    RecyclerView.Adapter<NearByLandAdapter.MyViewHolder>() {

    private var listNearby: List<NearbyLandmark> = arrayListOf()
    fun setListNearbyLandmark(list: List<NearbyLandmark>) {
        listNearby = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NearByLandAdapter.MyViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.items_nearby_land, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listNearby.size
    }

    override fun onBindViewHolder(holder: NearByLandAdapter.MyViewHolder, position: Int) {
        val current = listNearby[position]
        holder.bind(current)
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvNameRoomNearby: TextView = itemView.findViewById(R.id.tvNameRoomNearby)
        private val tvDistance: TextView = itemView.findViewById(R.id.tvDistance)

        fun bind(nearbyLandmark: NearbyLandmark) {
            tvNameRoomNearby.text = nearbyLandmark.place
            tvDistance.text = nearbyLandmark.distance.toString()
        }
    }
}