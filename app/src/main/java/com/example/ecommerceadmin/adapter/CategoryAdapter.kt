package com.example.ecommerceadmin.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ecommerceadmin.CatogeryModel
import com.example.ecommerceadmin.R
import com.example.ecommerceadmin.databinding.ItemCatogeryLayoutBinding

class CategoryAdapter (var context: Context, val list: ArrayList<CatogeryModel>) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

     inner class  CategoryViewHolder(view : View) : RecyclerView.ViewHolder(view){
        var binding = ItemCatogeryLayoutBinding.bind(view)
    }

   override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
       return CategoryViewHolder(LayoutInflater.from(context).inflate(R.layout.item_catogery_layout, parent,false))
    }

   override fun getItemCount(): Int {
        return list.size
    }

   override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.binding.textView2.text = list[position].cat
        Glide.with(context).load(list[position].img).into(holder.binding.imageView2)
    }

}