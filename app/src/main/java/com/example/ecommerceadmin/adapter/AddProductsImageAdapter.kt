package com.example.ecommerceadmin.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.ecommerceadmin.databinding.ImgItemBinding


class AddProductsImageAdapter (val list : ArrayList<Uri>) :
    RecyclerView.Adapter<AddProductsImageAdapter.AddProductsImageViewHolder>() {
    inner class AddProductsImageViewHolder(val binding: ImgItemBinding ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddProductsImageViewHolder {
       val binding = ImgItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return AddProductsImageViewHolder(binding)
    }

    override fun getItemCount(): Int {
       return list.size
    }

    override fun onBindViewHolder(holder: AddProductsImageViewHolder, position: Int) {
      holder.binding.itemImg.setImageURI(list[position])
    }
}