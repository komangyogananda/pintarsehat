package com.kulguy.pintarsehat.fragments

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.text.FirebaseVisionCloudTextRecognizerOptions
import com.kulguy.pintarsehat.R
import com.kulguy.pintarsehat.activities.FullPageSearchActivity
import pub.devrel.easypermissions.EasyPermissions
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


/**
 * A simple [Fragment] subclass.
 */
class PhotoFragment : Fragment(), EasyPermissions.PermissionCallbacks {

    private val OPERATION_CAPTURE_PHOTO = 1
    private val OPERATION_CHOOSE_PHOTO = 2
    private var mUri: Uri? = null
    private lateinit var contohGambar: ImageView
    private lateinit var currentPhotoPath: String
    private lateinit var firebaseImage: FirebaseVisionImage

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_photo, container, false)
        val cameraButton = view.findViewById<LinearLayout>(R.id.button_open_camera)
        val galleryButton = view.findViewById<LinearLayout>(R.id.button_open_gallery)
        contohGambar = view.findViewById(R.id.contoh_gambar)
        cameraButton.setOnClickListener {
            openCamera()
        }
        galleryButton.setOnClickListener {
            pickFromGallery()
        }
        return view
    }

    private fun hasCameraPermission(): Boolean {
        return if (context != null){
            EasyPermissions.hasPermissions(context!!, Manifest.permission.CAMERA)
        }else{
            false
        }
    }

    private fun openCamera(){
        if (hasCameraPermission()){
            Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
                takePictureIntent.resolveActivity(activity?.packageManager!!)?.also { componentName ->
                    val photoFile: File? = try {
                        createImageFile()
                    } catch (ex: IOException) {
                        null
                    }
                    photoFile?.also {file ->
                        val photoURI: Uri = FileProvider.getUriForFile(
                            activity!!,
                            "com.kulguy.pintarsehat.fileprovider",
                            file
                        )
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                        startActivityForResult(takePictureIntent, OPERATION_CAPTURE_PHOTO)
                    }
                }
            }
        }else{
            EasyPermissions.requestPermissions(
                this,
                "Izin kamera dan akses pada file storage anda diperlukan untuk mengakses fitur ini",
                OPERATION_CAPTURE_PHOTO,
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == OPERATION_CAPTURE_PHOTO && resultCode == RESULT_OK) {
//            val imageBitmap = data?.extras?.get("data") as Bitmap
//            contohGambar.setImageBitmap(imageBitmap)
            setPic()
        }else if (requestCode == OPERATION_CHOOSE_PHOTO && resultCode == RESULT_OK){
            val selectedImage = data?.data!!
            val filePathColumn =
                arrayOf(MediaStore.Images.Media.DATA)
            val cursor: Cursor =
                activity?.contentResolver?.query(selectedImage, filePathColumn, null, null, null)!!
            cursor.moveToFirst()
            val columnIndex = cursor.getColumnIndex(filePathColumn[0])
            val imgDecodableString = cursor.getString(columnIndex)
            cursor.close()
            val bitmap = BitmapFactory.decodeFile(imgDecodableString)
//            contohGambar.setImageBitmap(bitmap)
            processBitmap(bitmap)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        Log.w("PermissionDenied", perms.toString())
        view?.let { Snackbar.make(it, "Izin kamera dan storage dibutuhkan untuk fitur ini", Snackbar.LENGTH_LONG).show() }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        if (perms.containsAll(arrayListOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ))){
            if (requestCode == OPERATION_CAPTURE_PHOTO){
                openCamera()
            }else if (requestCode == OPERATION_CHOOSE_PHOTO){
                pickFromGallery()
            }
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File = context?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
        Log.w("FilesDir", storageDir.toString())
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }

    private fun pickFromGallery(){
        if (hasCameraPermission()){
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, OPERATION_CHOOSE_PHOTO)
        }else{
            EasyPermissions.requestPermissions(
                this,
                "Izin kamera dan akses pada file storage anda diperlukan untuk mengakses fitur ini",
                OPERATION_CHOOSE_PHOTO,
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        }
    }

    private fun setPic() {
        // Get the dimensions of the View
        val targetW: Int = 300
        val targetH: Int = 300

        val bmOptions = BitmapFactory.Options().apply {
            // Get the dimensions of the bitmap
            inJustDecodeBounds = true

            val photoW: Int = outWidth
            val photoH: Int = outHeight

            // Determine how much to scale down the image
            val scaleFactor: Int = Math.min(photoW / targetW, photoH / targetH)

            // Decode the image file into a Bitmap sized to fill the View
            inJustDecodeBounds = false
            inSampleSize = scaleFactor
            inPurgeable = true
        }
        BitmapFactory.decodeFile(currentPhotoPath, bmOptions)?.also { bitmap ->
//            contohGambar.setImageBitmap(bitmap)
            processBitmap(bitmap)
        }
    }

    private fun processBitmap(bitmap: Bitmap){
        firebaseImage = FirebaseVisionImage.fromBitmap(bitmap)
        val options = FirebaseVisionCloudTextRecognizerOptions.Builder()
            .setLanguageHints(listOf("id", "en"))
            .build()
        val detector = FirebaseVision.getInstance().getCloudTextRecognizer(options)
        val result = detector.processImage(firebaseImage)
            .addOnSuccessListener {
                Log.w("Result Firebase", it.text)
                if (it.textBlocks.size > 0){
                    Log.w("Blocks[0]", it.textBlocks[0].text)
                    val intent = Intent(context, FullPageSearchActivity::class.java)
                    intent.putExtra("SearchValue", it.textBlocks[0].text)
                    startActivity(intent)
                }else{
                    Toast.makeText(context, "Tidak ada kata terdeteksi, Upload ulang foto anda!", Toast.LENGTH_LONG).show()
                }
            }
            .addOnFailureListener {
                Toast.makeText(context, "Terjadi suatu kesalahan, Coba lagi beberapa saat lagi", Toast.LENGTH_LONG).show()
            }
    }

}
