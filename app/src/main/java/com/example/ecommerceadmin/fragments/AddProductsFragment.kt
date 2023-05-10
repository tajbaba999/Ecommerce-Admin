package com.example.ecommerceadmin.fragments

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Binder
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.ecommerceadmin.R
import com.example.ecommerceadmin.adapter.AddProductsImageAdapter
import com.example.ecommerceadmin.databinding.FragmentAddProductsBinding
import com.example.ecommerceadmin.model.AddProductModel
import com.example.ecommerceadmin.model.CatogeryModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.util.*
import java.util.Locale.Category
import kotlin.collections.ArrayList

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AddProductsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddProductsFragment : Fragment() {

    private lateinit var binding : FragmentAddProductsBinding
    private lateinit var list : ArrayList<Uri>
    private lateinit var listImages : ArrayList<String>
    private lateinit var adapter : AddProductsImageAdapter
    private  var coverImage : Uri? = null
    private lateinit var dialog: Dialog
    private var coverImgeUrl : String?= ""
    private lateinit var catogeryList: ArrayList<String>


    private var launchGalleryActivity = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){
        if (it.resultCode == Activity.RESULT_OK){
            coverImage = it.data!!.data
            binding.productCoverImg.setImageURI(coverImage)
            binding.productCoverImg.visibility =VISIBLE
        }
    }

    private var launchProductActivity = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){
        if (it.resultCode == Activity.RESULT_OK){
           val imgeurl = it.data!!.data
            list.add(imgeurl!!)
            adapter.notifyDataSetChanged()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
       binding = FragmentAddProductsBinding.inflate(layoutInflater)

        list = ArrayList()
        listImages = ArrayList()

        dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.progesslayout)
        dialog.setCancelable(false)

        binding.selectCoverImg.setOnClickListener {
            val intent = Intent("android.intent.action.GET_CONTENT")
            intent.type ="image/*"
            launchGalleryActivity.launch(intent)
        }

        binding.productImgBtn.setOnClickListener {
            val intent = Intent("android.intent.action.GET_CONTENT")
            intent.type ="image/*"
            launchGalleryActivity.launch(intent)
        }
        setProductsCatogery()

        adapter = AddProductsImageAdapter(list)
        binding.productImgRecycleView.adapter = adapter

        binding.submitProductsBtn.setOnClickListener {
            validateData()
        }

        return binding.root
    }

    private fun validateData() {
        if (binding.productName.text.toString().isEmpty()){
            binding.productName.requestFocus()
            binding.productName.error = "Empty"
        }else if (binding.productsp.text.toString().isEmpty()){
            binding.productsp.requestFocus()
            binding.productsp.error = "Empty"
        }else if(coverImage == null){
            Toast.makeText(requireContext(),"Please select cover image",Toast.LENGTH_LONG).show()
        }
        else if (listImages.size < 1){
            Toast.makeText(requireContext(),"Please select product image",Toast.LENGTH_LONG).show()

        }else{
            uploadImage()
        }
    }

    private fun uploadImage() {
        dialog.show()

        val fileName  = UUID.randomUUID().toString()+".jpg"

        val refStorge = FirebaseStorage.getInstance().reference.child("products/$fileName")
        refStorge.putFile(coverImage!!)
            .addOnSuccessListener {
                it.storage.downloadUrl.addOnSuccessListener { image ->
                    coverImgeUrl = image.toString()

                    uploadProductImage()
                }
            }
            .addOnFailureListener{
                dialog.dismiss()
                Toast.makeText( requireContext(),"Something went wring with storage",Toast.LENGTH_SHORT).show()
            }
    }

    private var i = 0
    private fun uploadProductImage() {
        dialog.show()

        val fileName  = UUID.randomUUID().toString()+".jpg"

        val refStorge = FirebaseStorage.getInstance().reference.child("products/$fileName")
        refStorge.putFile(list[i])
            .addOnSuccessListener {
                it.storage.downloadUrl.addOnSuccessListener { image ->
                    listImages.add(image!!.toString())
                    if (list.size == listImages.size){
                        storeData()
                    }else{
                        i +=1
                        uploadProductImage()
                    }

                    storeData(categoryName,image.toString())
                }
            }
            .addOnFailureListener{
                dialog.dismiss()
                Toast.makeText( requireContext(),"Something went wring with storage",Toast.LENGTH_SHORT).show()
            }
    }

    private fun storeData() {

        val db = Firebase.firestore.collection("products")
        val key = db.id

        val data = AddProductModel(
            binding.productName.text.toString(),
            binding.productdesciption.text.toString()
            coverImgeUrl.toString()
            category
                    //1:30:46 - 10 sec

        )
    }

    private fun setProductsCatogery(){

        catogeryList = ArrayList()
        Firebase.firestore.collection("Categories").get().addOnSuccessListener {
            catogeryList.clear()
            for (doc in it.documents){
                val data = doc.toObject(CatogeryModel::class.java)
                catogeryList.add(data!!.cat!!)
            }
            catogeryList.add(0,"Select Category")

            val arrayAdapter = ArrayAdapter(requireContext(),R.layout.dropdown_item_layout, catogeryList)
            binding.productCatogeryDropdown.adapter =  arrayAdapter
        }
    }


}