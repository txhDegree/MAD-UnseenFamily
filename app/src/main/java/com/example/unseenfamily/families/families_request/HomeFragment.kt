package com.example.unseenfamily.families.families_request

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.unseenfamily.R
import com.example.unseenfamily.databinding.FragmentHomeBinding
import com.example.unseenfamily.families.families_request.tabs.VPAdapter
import com.example.unseenfamily.families.families_request.tabs.VPRecycleViewAdapter
import com.example.unseenfamily.viewModel.DonationViewModel
import com.google.android.material.tabs.TabLayoutMediator

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val donationViewModel: DonationViewModel by activityViewModels()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.floatingActionButtonAddRequest.setOnClickListener{
            donationViewModel.viewOnly = false
            findNavController().navigate(R.id.action_nav_my_request_to_nav_add_request)
        }

        val vpAdapter = VPAdapter(activity?.supportFragmentManager!!, lifecycle, VPRecycleViewAdapter.DonationOnClickListener{ donation ->
            donationViewModel.viewOnly = true
            donationViewModel.selectedDonation = donation
            findNavController().navigate(R.id.action_nav_my_request_to_nav_add_request)
        })
        binding.viewpager.adapter = vpAdapter
        TabLayoutMediator(binding.tabLayout, binding.viewpager){ tab, position->
            when(position){
                0 -> {
                    tab.text = "All Requests"
                }
                1 -> {
                    tab.text = "Pending"
                }
                2 -> {
                    tab.text = "Completed"
                }
            }
        }.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        return when(item.itemId) {
            R.id.action_sync -> {
                donationViewModel.reload()
                true
            }
            else -> false
        }
    }

}