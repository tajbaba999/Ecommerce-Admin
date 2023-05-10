package com.example.ecommerceadmin.fragments

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.ecommerceadmin.CatogeryModel
import com.example.ecommerceadmin.R
import com.example.ecommerceadmin.adapter.CategoryAdapter
import com.example.ecommerceadmin.databinding.FragmentCategoryBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.util.*
import kotlin.collections.ArrayList


class CategoryFragment : Fragment() {

    private lateinit var binding : FragmentCategoryBinding
    private var imgeurl : Uri? = null
    private lateinit var dialog: Dialog

    private var launchGalleryActivity = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){
        if (it.resultCode == Activity.RESULT_OK){
            imgeurl = it.data!!.data
            binding.imageView.setImageURI(imgeurl)
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCategoryBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.progesslayout)
        dialog.setCancelable(false)

        getData()

        binding.apply {
            imageView.setOnClickListener {
                val intent = Intent("android.intent.action.GET_CONTENT")
                intent.type ="image/*"
                launchGalleryActivity.launch(intent)
            }
            btncatogery.setOnClickListener {
                validateData(binding.catogeryedit.text.toString())
            }
        }
        return binding.root
    }

 private fun getData(){
     var list = ArrayList<CatogeryModel>()
     Firebase.firestore.collection("categories")
         .get().addOnSuccessListener {
             list.clear()
             for (doc in it.documents){
                 val data = doc.toObject(CatogeryModel::class.java)
                 list.add(data!!)
             }
             binding.categoryrecyclerView.adapter = CategoryAdapter(requireContext(),list)
         }
 }

    private fun validateData(categoryName : String) {
        if (categoryName.isEmpty()){
            Toast.makeText(requireContext(),"Please provide categeory name",Toast.LENGTH_SHORT).show()
        }
        else if(imgeurl == null){
            Toast.makeText(requireContext(),"Please select image",Toast.LENGTH_SHORT).show()
        }
        else{
            uploadImage(categoryName)
        }

    }

    private fun uploadImage(categoryName: String) {
        dialog.show()

        val fileName  = UUID.randomUUID().toString()+".jpg"

        val refStorge = FirebaseStorage.getInstance().reference.child("category/$fileName")
        refStorge.putFile(imgeurl!!)
            .addOnSuccessListener {
                it.storage.downloadUrl.addOnSuccessListener { image ->
                    storeData(categoryName,image.toString())
                }
            }
            .addOnFailureListener{
                dialog.dismiss()
                Toast.makeText( requireContext(),"Something went wring with storage",Toast.LENGTH_SHORT).show()
            }
    }

    private fun storeData(categoryName: String,url: String) {
        val db = Firebase.firestore

        val data = hashMapOf<String,Any>(
            "cate" to categoryName,
            "img" to url
        )
        db.collection("categories").add(data)
            .addOnSuccessListener {
                dialog.dismiss()
                binding.imageView.setImageDrawable(resources.getDrawable(R.drawable.preview_image))
                binding.catogeryedit.text = null
                getData()
                Toast.makeText(requireContext(),"Catogery Added",Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(requireContext(),"Something went worng",Toast.LENGTH_SHORT).show()

            }
    }
    }
