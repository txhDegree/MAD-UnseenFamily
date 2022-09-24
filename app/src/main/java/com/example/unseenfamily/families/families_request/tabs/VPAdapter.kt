package com.example.unseenfamily.families.families_request.tabs

import com.example.unseenfamily.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.unseenfamily.entities.Donation

class VPAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle, val itemsOnClickListener: VPRecycleViewAdapter.DonationOnClickListener) : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> {
                AllRequestFragment(itemsOnClickListener)
            }
            1 -> {
                PendingFragment(itemsOnClickListener)
            }
            2 -> {
                CompletedFragment(itemsOnClickListener)
            }
            else -> {
                Fragment()
            }
        }
    }



}