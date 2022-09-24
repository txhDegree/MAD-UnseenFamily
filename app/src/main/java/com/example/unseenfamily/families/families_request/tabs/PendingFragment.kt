package com.example.unseenfamily.families.families_request.tabs

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.unseenfamily.R
import com.example.unseenfamily.databinding.FragmentAllRequestsBinding
import com.example.unseenfamily.viewModel.DonationViewModel

class PendingFragment (private val itemsOnClickListener: VPRecycleViewAdapter.DonationOnClickListener) : Fragment() {

    private var _binding: FragmentAllRequestsBinding? = null
    private val binding get() = _binding!!
    private val donationViewModel: DonationViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAllRequestsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        val recycleViewAdapter = VPRecycleViewAdapter(itemsOnClickListener)

        donationViewModel.pendingDonationList.observe(viewLifecycleOwner){
            if(it.isEmpty())
                binding.textViewNoRecord.setText(R.string.no_record)
            else
                binding.textViewNoRecord.text = ""
            recycleViewAdapter.setDonation(it)
        }

        binding.recycleViewDonation.layoutManager = LinearLayoutManager(activity?.applicationContext)
        binding.recycleViewDonation.adapter = recycleViewAdapter
        super.onResume()
    }
}