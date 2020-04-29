package com.kulguy.pintarsehat.fragments

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.kulguy.pintarsehat.R
import kotlinx.android.synthetic.main.fragment_photo.*
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions


/**
 * A simple [Fragment] subclass.
 */
class PhotoFragment : Fragment(), EasyPermissions.PermissionCallbacks {

    companion object {
        const val REQUEST_IMAGE_CAPTURE = 200
    }

    private var currentPhotoPath = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_photo, container, false)
        val cameraButton = view.findViewById<LinearLayout>(R.id.button_open_camera)
        cameraButton.setOnClickListener {
            validatePermissions()
        }
        return view
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        view?.let { Snackbar.make(it, "Izin kamera dibutuhkan", Snackbar.LENGTH_LONG).show() }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        launchCamera()
    }

    @AfterPermissionGranted(REQUEST_IMAGE_CAPTURE)
    private fun validatePermissions() {
        val permissions = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE)

        if (context?.let { EasyPermissions.hasPermissions(it, *permissions) }!!){
            launchCamera()
        } else {
            EasyPermissions.requestPermissions(this,
                "Izin kamera",
                REQUEST_IMAGE_CAPTURE, *permissions)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    private fun launchCamera() {
        val values = ContentValues(1)
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpg")
        val fileUri =
            context?.contentResolver?.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        if (intent.resolveActivity(context!!.packageManager) != null) {
            currentPhotoPath = fileUri.toString()
            Log.w("Result path", currentPhotoPath)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION
                    or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.w("Result", data.toString())
        Log.w("Result Code", resultCode.toString())
        Log.w("Result Reqcode", requestCode.toString())
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_IMAGE_CAPTURE) {
            val photo = data!!.extras!!["data"] as Bitmap?
            Log.w("Result2", photo.toString())
//            photo_fragment_illustration.setImageBitmap(photo)
        }
    }

}
