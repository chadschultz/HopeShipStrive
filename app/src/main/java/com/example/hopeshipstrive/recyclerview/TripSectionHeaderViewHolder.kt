package com.example.hopeshipstrive.recyclerview

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.hopeshipstrive.R

class TripSectionHeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val dateTextView: TextView = itemView.findViewById(R.id.dateTextView)
    private val startTimeTextView: TextView = itemView.findViewById(R.id.startTimeTextView)
    private val endTimeTextView: TextView = itemView.findViewById(R.id.endTimeTextView)
    private val estEarningsTextView: TextView = itemView.findViewById(R.id.estEarningsValueTextView)

    fun bind(header: TripListItem.HeaderItem) {
        dateTextView.text = header.formattedDate
        startTimeTextView.text = header.formattedStartTime
        endTimeTextView.text = header.formattedEndTime
        estEarningsTextView.text = header.formattedEarnings
    }
}