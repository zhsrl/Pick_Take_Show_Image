package com.e.pickimage

import android.annotation.SuppressLint
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import java.io.File

class MainActivity : AppCompatActivity() {

    private lateinit var pickFromAlbum: TextView
    private lateinit var takeFromCamera: TextView
    private lateinit var showImage: ImageView
    private lateinit var pathImage: TextView

    val PICK_IMAGE = 1
    val TAKE_IMAGE = 2

    @SuppressLint("IntentReset")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()

        pickFromAlbum.setOnClickListener{
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            intent.setType("image/*")
            intent.setAction(Intent.ACTION_GET_CONTENT)
            startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE)


        }

        takeFromCamera.setOnClickListener{
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(Intent.createChooser(intent, "Take a photo"), TAKE_IMAGE)
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode != RESULT_CANCELED){
            if(requestCode == TAKE_IMAGE && data != null && resultCode == RESULT_OK){
                val selectedImage: Bitmap = data.extras!!.get("data") as Bitmap
                showImage.setImageBitmap(selectedImage)

            }
            else if(requestCode == PICK_IMAGE && data != null && resultCode == RESULT_OK){
                val selectedImage: Uri? = data.data!!

                if(selectedImage != null){

                    val imgBitmap: Bitmap = BitmapFactory.decodeStream(contentResolver.openInputStream(selectedImage))
                    val file = File(selectedImage.path!!)

                    val path = file.path.toString()
                    pathImage.text = path
                    Log.e("PATH FILE : ", path)

                    Log.e("URI : ", selectedImage.toString())
                    showImage.setImageBitmap(imgBitmap)


                }
            }
        }


    }

    fun init(){
        pickFromAlbum = findViewById(R.id.TV_pick_from_album)
        takeFromCamera = findViewById(R.id.TV_take_from_camera)
        showImage = findViewById(R.id.imageView)
        pathImage = findViewById(R.id.TV_image_path)
    }
}