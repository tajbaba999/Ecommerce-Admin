package com.example.ecommerceadmin.fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.app.Instrumentation.ActivityResult
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import com.example.ecommerceadmin.R
import com.example.ecommerceadmin.databinding.FragmentHomeBinding
import com.example.ecommerceadmin.databinding.FragmentSliderBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID


class SliderFragment : Fragment() {

    private lateinit var binding: FragmentSliderBinding
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
    @SuppressLint("SuspiciousIndentation")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = FragmentSliderBinding.inflate(layoutInflater)

            dialog = Dialog(requireContext())
            dialog.setContentView(R.layout.progesslayout)
            dialog.setCancelable(false)


        binding.apply {
            imageView.setOnClickListener {
                val intent = Intent("android.intent.action.GET_CONTENT")
                intent.type ="image/*"
                launchGalleryActivity.launch(intent)
            }

        btnslider.setOnClickListener {
            if (imgeurl != null){

                uploadImage(imgeurl!!)

            }else{
                Toast.makeText(requireContext(), "Please select image", Toast.LENGTH_SHORT).show()
            }
        }

        }


        return binding.root
    }

    private fun uploadImage(uri: Uri) {
        dialog.show()

        val fileName  = UUID.randomUUID().toString()+".jpg"

        val refStorge = FirebaseStorage.getInstance().reference.child("slider/$fileName")
        refStorge.putFile(uri)
            .addOnSuccessListener {
                it.storage.downloadUrl.addOnSuccessListener { image ->
                    storeData(image.toString())
                }
            }
            .addOnFailureListener{
                dialog.dismiss()
                Toast.makeText( requireContext(),"Something went wring with storage",Toast.LENGTH_SHORT).show()
            }
    }

    private fun storeData(image: String) {
        val db = Firebase.firestore

        val data = hashMapOf<String,Any>(
            "img" to image
        )
        db.collection("slider").document("item").set(data)
            .addOnSuccessListener {
                dialog.dismiss()
                Toast.makeText(requireContext(),"Slider update",Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(requireContext(),"Something went worng",Toast.LENGTH_SHORT).show()

            }
    }


}