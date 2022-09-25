package com.example.unseenfamily.families.families_request.tabs

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.unseenfamily.R
import com.example.unseenfamily.entities.Donation
import com.example.unseenfamily.entities.DonationWithDonationItems

class VPRecycleViewAdapter(private val onClickListener: DonationOnClickListener, val context: Context): RecyclerView.Adapter<VPRecycleViewAdapter.ViewHolder>() {

    private var dataSet = emptyList<DonationWithDonationItems>()

    class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
        val textViewTitle : TextView = view.findViewById<TextView>(R.id.textViewDonationItemTitle)
        val textViewDescription: TextView = view.findViewById<TextView>(R.id.textViewDonationItemDescription)
        val imageViewStatus: ImageView = view.findViewById<ImageView>(R.id.imageViewStatus)
    }

    internal fun setDonation(donationWithDonationItems: List<DonationWithDonationItems>) { // link new list to dataset during run time
        dataSet = donationWithDonationItems
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.donation_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val donationWithDonationItems = dataSet[position]

        with(donationWithDonationItems){
            holder.textViewTitle.text = donation.title
            holder.textViewDescription.text = donation.description
            holder.imageViewStatus.setImageDrawable(context.resources.getDrawable(when(donation.status){
                "y" -> R.drawable.ic_baseline_check_circle_24
                "n" -> R.drawable.ic_baseline_cancel_24
                else -> R.drawable.ic_baseline_check_circle_24
            }))
            holder.itemView.setOnClickListener{
                onClickListener.onClick(donationWithDonationItems)
            }
        }
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    class DonationOnClickListener (val clickListener: (donationWithDonationItems: DonationWithDonationItems) -> Unit) {
        fun onClick(donationWithDonationItems: DonationWithDonationItems) = clickListener(donationWithDonationItems)
    }

}