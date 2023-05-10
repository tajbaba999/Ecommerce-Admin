package com.example.ecommerceadmin.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.ecommerceadmin.R
import com.example.ecommerceadmin.activity.AllordersActivity
import com.example.ecommerceadmin.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {

    private lateinit var binding : FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(layoutInflater)

        binding.catogery.setOnClickListener {
                findNavController().navigate(R.id.action_homeFragment_to_categoryFragment)
        }
        binding.orderdetails.setOnClickListener {
            val intent = Intent(requireContext(),AllordersActivity::class.java)
            startActivity(intent)
        }
        binding.products.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_productFragment)
        }
        binding.slider.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_sliderFragment)
        }


        return binding.root
    }

}