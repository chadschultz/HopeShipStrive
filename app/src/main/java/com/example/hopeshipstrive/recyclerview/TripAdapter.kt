package com.example.hopeshipstrive.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hopeshipstrive.R

class TripAdapter(
    private val trips: List<TripListItem>,
    private val listener: OnItemClickListener,
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            TYPE_HEADER -> TripSectionHeaderViewHolder(
                inflater.inflate(R.layout.trip_section_header, parent, false)
            )
            TYPE_TRIP -> TripViewHolder(
                inflater.inflate(R.layout.trip_card, parent, false)
            )
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is TripSectionHeaderViewHolder -> {
                val headerItem = trips[position] as TripListItem.HeaderItem
                holder.bind(headerItem)
            }
            is TripViewHolder -> {
                val tripItem = trips[position] as TripListItem.TripItem
                holder.itemView.setOnClickListener { listener.onItemClick(tripItem, position) }
                holder.bind(tripItem)
            }
        }
    }

    override fun getItemCount(): Int {
        return trips.size
    }

    override fun getItemViewType(position: Int): Int {
        return when (trips[position]) {
            is TripListItem.HeaderItem -> TYPE_HEADER
            is TripListItem.TripItem -> TYPE_TRIP
        }
    }

    companion object {
        private const val TYPE_HEADER = 0
        private const val TYPE_TRIP = 1
    }

    interface OnItemClickListener {
        fun onItemClick(trip: TripListItem.TripItem, position: Int)
    }
}