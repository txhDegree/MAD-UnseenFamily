package com.example.unseenfamily.families.add_request

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.unseenfamily.R
import com.example.unseenfamily.adapter.DonationItemAdapter
import com.example.unseenfamily.databinding.FragmentAddRequestBinding
import com.example.unseenfamily.entities.Donation
import com.example.unseenfamily.entities.DonationItem
import com.example.unseenfamily.entities.DonationWithDonationItems
import com.example.unseenfamily.viewModel.DonationViewModel


class AddRequestFragment : Fragment() {

    private var _binding : FragmentAddRequestBinding? = null

    private val binding get() = _binding!!

    private val donationViewModel: DonationViewModel by activityViewModels()

    private lateinit var donationItemAdapter : DonationItemAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddRequestBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        donationItemAdapter = DonationItemAdapter(!donationViewModel.viewOnly)
        super.onViewCreated(view, savedInstanceState)

        binding.recycleViewDonationListItem.layoutManager = LinearLayoutManager(activity?.applicationContext)
        binding.recycleViewDonationListItem.adapter = donationItemAdapter

        binding.editTextNeededItem.visibility = if(donationViewModel.viewOnly) View.INVISIBLE else View.VISIBLE
        binding.editTextNumberQuantity.visibility = if(donationViewModel.viewOnly) View.INVISIBLE else View.VISIBLE
        binding.buttonAdd.visibility = if(donationViewModel.viewOnly) View.INVISIBLE else View.VISIBLE
        binding.buttonSave.visibility = if(donationViewModel.viewOnly) View.INVISIBLE else View.VISIBLE

        binding.editTextDescription.isEnabled = !donationViewModel.viewOnly
        binding.editTextTitle.isEnabled = !donationViewModel.viewOnly
        donationItemList.clear()

        if(donationViewModel.viewOnly){
            with(donationViewModel.selectedDonation){
                binding.editTextTitle.setText(donation.title)
                binding.editTextDescription.setText(donation.description)
                donationItemList.addAll(donationItems)
                donationItemAdapter.setDonationItem(donationItems)
            }
            (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.view_request)
        } else {
            binding.buttonAdd.setOnClickListener{
                val item = binding.editTextNeededItem
                val itemText = item.text.toString()
                val quantity = binding.editTextNumberQuantity
                val quantityText = quantity.text.toString()
                val tempItem = DonationItem(name = "$itemText x $quantityText")
                donationItemList.add(tempItem)
                donationItemAdapter.setDonationItem(donationItemList)

                item.text.clear()
                quantity.text.clear()

                Toast.makeText(context, getString(R.string.item_added), Toast.LENGTH_SHORT).show()
            }

            binding.buttonSave.setOnClickListener {
                val titleText = binding.editTextTitle.text.toString()
                if(titleText.isEmpty()){
                    binding.editTextTitle.error = getString(R.string.title_required)
                    binding.editTextTitle.requestFocus()
                    return@setOnClickListener
                }
                val descriptionText = binding.editTextDescription.text.toString()
                if(descriptionText.isEmpty()){
                    binding.editTextDescription.error = getString(R.string.description_required)
                    binding.editTextDescription.requestFocus()
                    return@setOnClickListener
                }
                if(donationItemList.isEmpty()){
                    Toast.makeText(context, getString(R.string.items_required), Toast.LENGTH_SHORT).show()
                }
                val tempDonation = DonationWithDonationItems( Donation(titleText, descriptionText, "123"), donationItemList.toList() )
                donationViewModel.insert(tempDonation)
                Toast.makeText(context, getString(R.string.new_donation_added), Toast.LENGTH_SHORT).show()
                findNavController().navigateUp()
            }
        }
    }

    override fun onResume() {
        donationItemAdapter.setDonationItem(donationItemList)
        super.onResume()
    }

    companion object {
        val donationItemList: MutableList<DonationItem> = emptyList<DonationItem>().toMutableList()
    }

}