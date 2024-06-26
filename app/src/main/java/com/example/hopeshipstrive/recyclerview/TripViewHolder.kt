package com.example.hopeshipstrive.recyclerview

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.hopeshipstrive.R

class TripViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val pickupTimeTextView: TextView = itemView.findViewById(R.id.pickupTimeTextView)
    private val dropOffTimeTextView: TextView = itemView.findViewById(R.id.dropOffTimeTextView)
    private val riderTextView: TextView = itemView.findViewById(R.id.riderTextView)
    private val estimatedEarningsValueTextView: TextView = itemView.findViewById(R.id.estEarningsValueTextView)
    private val waypointsTextView: TextView = itemView.findViewById(R.id.waypointsTextView)

    fun bind(trip: TripListItem.TripItem) {
        pickupTimeTextView.text = trip.formattedPickupTime
        dropOffTimeTextView.text = trip.formattedDropoffTime
        riderTextView.text = formatRiderText(trip.numRiders, trip.numBoosters)
        estimatedEarningsValueTextView.text = trip.formattedEarnings
        waypointsTextView.text = trip.formattedWaypoints
    }

    private fun formatRiderText(numRiders: Int, numBoosters: Int): String {
        val resources = itemView.resources

        val riderText = resources.getQuantityString(R.plurals.rider, numRiders)
        return if (numBoosters > 0) {
            val boosterText = resources.getQuantityString(R.plurals.booster, numBoosters)
            resources.getString(R.string.riders_with_boosters, numRiders, riderText, numBoosters, boosterText)
        } else {
            resources.getString(R.string.riders_no_boosters, numRiders, riderText)
        }
    }
}