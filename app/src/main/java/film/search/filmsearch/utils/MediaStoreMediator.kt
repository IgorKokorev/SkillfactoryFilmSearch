package film.search.filmsearch.utils

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

object MediaStoreMediator {
    // This function takes a bitmap, a context, and a folder name as parameters
    // It returns the Uri of the saved image or null if there is an error
    fun saveBitmapToGallery(
        bitmap: Bitmap,
        context: Context,
        folderName: String,
        fileName: String
    ): Uri? {
        // Declare a variable to store the output stream
        val outputStream: OutputStream?
        // Check the Android version
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // For Android Q and above, use the MediaStore API
            // Create a content values object to store the image metadata
            val values = ContentValues().apply {
                // Set the MIME type, date, relative path, and display name
                put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis() / 1000)
                put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis())
                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/$folderName")
                put(MediaStore.Images.Media.DISPLAY_NAME, "${fileName.handleSingleQuote()}.jpg")
                // Set the pending flag to true to indicate the image is not ready yet
                put(MediaStore.Images.Media.IS_PENDING, true)
            }
            // Get the content resolver and insert the values to get the image Uri
            val resolver = context.contentResolver
            val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
            // If the Uri is not null, try to open the output stream
            if (uri != null) {
                outputStream = try {
                    resolver.openOutputStream(uri)
                } catch (e: Exception) {
                    null
                }
                // Compress the bitmap to the output stream
                if (outputStream != null) {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                }
                // Close the output stream
                outputStream?.close()
                // Set the pending flag to false to indicate the image is ready
                values.put(MediaStore.Images.Media.IS_PENDING, false)
                // Update the Uri with the new values
                resolver.update(uri, values, null, null)
                // Return the Uri
                return uri
            }
        } else { // For Android below Q, use the File API
            // Create a directory in the external storage with the given folder name
            val directory = File(
                Environment.getExternalStorageDirectory().toString() + File.separator + folderName
            )
            // If the directory does not exist, make it
            if (!directory.exists()) {
                directory.mkdirs()
            }
            // Create a file object with the directory and the file name
            val file = File(directory, "${fileName.handleSingleQuote()}.jpg")
            // Try to open the output stream from the file
            outputStream = FileOutputStream(file)
            // Compress the bitmap to the output stream
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            // Close the output stream
            outputStream.close()
            // If the file path is not null, create a content values object to store the image metadata
//            if (file.absolutePath != null) {
            val values = ContentValues().apply {
                // Set the MIME type and the data path
                put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                put(MediaStore.Images.Media.DATA, file.absolutePath)
            }
            // Insert the values to the content resolver to get the image Uri
            val uri =
                context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
            // Return the Uri
            return uri
//            }
        }
        // If any of the above steps failed, return null
        return null
    }

    // Remove quotes from string
    private fun String.handleSingleQuote(): String {
        return this.replace("'", "")
    }
}