package com.amanotes.classicalpian.model.source

import android.content.Context
import java.io.File
import java.io.FileWriter
import java.io.IOException

interface FileManager {

    fun createCryptoFile(encryptedData: String): File?

}

class FileManagerImpl(private val context: Context) : FileManager {

    private val fileName = "Service_File.txt"

    override fun createCryptoFile(encryptedData: String): File? {
        return try {
            val file = File(context.filesDir, fileName)
            FileWriter(file, false).use { writer ->
                writer.write(encryptedData)
                writer.flush()
            }
            file
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

}