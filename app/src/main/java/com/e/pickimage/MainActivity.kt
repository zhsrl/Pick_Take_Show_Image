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
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var pickFromAlbum: TextView
    private lateinit var takeFromCamera: TextView
    private lateinit var showImage: ImageView

    val PICK_IMAGE = 1
    val TAKE_IMAGE = 2

    @SuppressLint("IntentReset")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()

        pickFromAlbum.setOnClickListener{
            val intent = Intent()
            intent.setType("image/*")
            intent.setAction(Intent.ACTION_GET_CONTENT)
            startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE)


        }

        takeFromCamera.setOnClickListener{


        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode != RESULT_CANCELED){
            if(requestCode == TAKE_IMAGE && data != null && resultCode == RESULT_OK){
                val selectedImage: Bitmap = data.extras!!.get("data") as Bitmap
                showImage.setImageBitmap(selectedImage)

            }
            else if(requestCode == PICK_IMAGE && data != null && resultCode == RESULT_OK){
                val selectedImage: Uri = data.data!!
                val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)

                if(selectedImage != null){
                    val cursor: Cursor? = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        contentResolver.query(selectedImage,
                                filePathColumn,
                                null,
                                null)
                    } else {
                        TODO("VERSION.SDK_INT < O")
                    }



                    if(cursor != null){
                        cursor.moveToFirst()

                        val imgIndex: String? = cursor.getString(cursor.getColumnIndex(filePathColumn[0]))
                        showImage.setImageBitmap(BitmapFactory.decodeFile(imgIndex))

                        cursor.close()
                    }
                }
            }
        }


    }

    fun init(){
        pickFromAlbum = findViewById(R.id.TV_pick_from_album)
        takeFromCamera = findViewById(R.id.TV_take_from_camera)
        showImage = findViewById(R.id.imageView)
    }
}