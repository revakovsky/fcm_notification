package com.amanotes.classicalpian.model.source

import com.amanotes.classicalpian.model.dto.EncryptedData
import com.ionspin.kotlin.crypto.LibsodiumInitializer
import com.ionspin.kotlin.crypto.secretbox.SecretBox
import com.ionspin.kotlin.crypto.secretstream.crypto_secretstream_xchacha20poly1305_KEYBYTES
import com.ionspin.kotlin.crypto.util.LibsodiumRandom
import com.ionspin.kotlin.crypto.util.encodeToUByteArray
import com.ionspin.kotlin.crypto.util.toHexString
import kotlin.experimental.xor

interface CryptoManager {

    fun encryptStringData(referrerString: String, gadId: String): EncryptedData
    fun encryptOrDecryptString(value: String): String

}

class CryptoManagerImpl : CryptoManager {

    private val xorKey: Byte = 0x0D

    @OptIn(ExperimentalUnsignedTypes::class)
    override fun encryptStringData(referrerString: String, gadId: String): EncryptedData {
        var encryptedData = EncryptedData()
        LibsodiumInitializer.initializeWithCallback {
            val referrerBytes = referrerString.encodeToUByteArray()
            val key = LibsodiumRandom
                .buf(crypto_secretstream_xchacha20poly1305_KEYBYTES)
                .toHexString()

            val nonce = LibsodiumRandom
                .buf(crypto_secretstream_xchacha20poly1305_KEYBYTES)
                .toHexString()

            val encrypted = SecretBox.easy(
                referrerBytes,
                nonce.encodeToUByteArray(),
                key.encodeToUByteArray()
            ).toHexString()

            encryptedData = EncryptedData(
                data = "$encrypted,$key,$nonce,$gadId",
                key = key,
                nonce = nonce
            )
        }
        return encryptedData
    }

    override fun encryptOrDecryptString(value: String): String {
        val bytes = value.toByteArray()
        for (i in bytes.indices) {
            bytes[i] = bytes[i] xor xorKey
        }
        return String(bytes)
    }

}

