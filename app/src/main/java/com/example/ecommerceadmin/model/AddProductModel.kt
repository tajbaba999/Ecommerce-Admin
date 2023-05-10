package com.example.ecommerceadmin.model

data class AddProductModel(
    val productName: String = "",
    val productDesciption : String = "",
    val productCoverImg : String = "",
    val productCatogery : String = "",
    val productId : String = "",
    val productMrp : String = "",
    val productSp : String = "",
    val productImgages : ArrayList<String> = ArrayList()
)
