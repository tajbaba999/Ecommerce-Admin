package com.example.ecommerceadmin.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.example.ecommerceadmin.R
import com.example.ecommerceadmin.databinding.FragmentCategoryBinding
import com.example.ecommerceadmin.databinding.FragmentProductBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProductFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProductFragment : Fragment() {

    private lateinit var binding: FragmentProductBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProductBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        binding.floatingActionButton.setOnClickListener {
                Navigation.findNavController(it).navigate(R.id.action_productFragment_to_addProductsFragment)
        }

        return binding.root
    }


}