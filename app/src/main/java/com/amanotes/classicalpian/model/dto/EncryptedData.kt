package com.amanotes.classicalpian.model.dto

data class EncryptedData(
    val data: String = "",
    val key: String = "",
    val nonce: String = ""
)
