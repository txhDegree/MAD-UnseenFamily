package com.example.unseenfamily.adapter
import android.view.LayoutInflater
import com.example.unseenfamily.R
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.unseenfamily.entities.DonationItem
import com.example.unseenfamily.families.add_request.AddRequestFragment

class DonationItemAdapter(private val showDelete: Boolean) : RecyclerView.Adapter<DonationItemAdapter.ViewHolder>(){

    private var dataSet = emptyList<DonationItem>()

    class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
        val textViewName : TextView = view.findViewById<TextView>(R.id.textViewName)
        val buttonDelete : Button = view.findViewById<Button>(R.id.buttonDelete)
    }

    internal fun setDonationItem(donationItem: List<DonationItem>) { // link new donationItem list to dataset during run time
        dataSet = donationItem
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.donation_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val donationItem = dataSet[position]
        holder.textViewName.text = donationItem.name
        if(showDelete){
            holder.buttonDelete.setOnClickListener{
                AddRequestFragment.donationItemList.remove(donationItem)
                setDonationItem(AddRequestFragment.donationItemList)
            }
            holder.buttonDelete.visibility = View.VISIBLE
        } else {
            holder.buttonDelete.visibility = View.INVISIBLE
        }
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

}