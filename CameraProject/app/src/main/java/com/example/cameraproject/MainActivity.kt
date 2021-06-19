package com.example.cameraproject

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.FileProvider
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    val REQUEST_IMAGE_CAPTURE = 1 // 카메라 사진찰영 요청 코드

    lateinit var curPhotoPath : String // 문자열 형태의 사진 경로 값


    lateinit var image: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setPermission()

        var camera = findViewById<Button>(R.id.btn_camera)
        image = findViewById(R.id.image)

        camera.setOnClickListener {
            takeCapture()

        }
    }

    private fun takeCapture() {
        // 기본 카메라 앱 실행

        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also {takePictureIntent->
            takePictureIntent.resolveActivity(packageManager)?.also {
                val photoFile : File? = try{
                    createImageFile()
            }catch (ex : IOException){
                null
            }
                photoFile?.also {
                    val photoURI : Uri = FileProvider.getUriForFile(
                            this,
                            "com.example.cameraproject.fileprovider",
                            it
                    )

                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,photoURI)
                            startActivityForResult(takePictureIntent,REQUEST_IMAGE_CAPTURE)

                }
            }
        }
    }

    // 이미지 파일 생성
    private fun createImageFile(): File {
    val timestamp : String  = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
//        val storageDir : File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val storageDir : File? = this.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile("JPEG_${timestamp}_",".jpg",storageDir)
                .apply {
                    curPhotoPath = absolutePath
                }
    }


    //    테트퍼미션 설정
    private fun setPermission() {

        val permission = object :PermissionListener
        {
            override fun onPermissionGranted() {

                Toast.makeText(applicationContext,"권한이 허용되었습니다",Toast.LENGTH_SHORT).show()
            }

            override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                Toast.makeText(applicationContext,"권한이 거부 되었습니다",Toast.LENGTH_SHORT).show()
            }
        }
        TedPermission.with(this)
                .setPermissionListener(permission)
                .setRationaleMessage("카메라 앱을 사용하시려면 권한을 허용해주세요")
                .setDeniedMessage("권한이 거부하셨습니다")
                .setPermissions(android.Manifest.permission.CAMERA,android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .check()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {


        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK){
            //  이미지를 성공적으로 가져왔다면

            val bitmap : Bitmap

            val file = File(curPhotoPath)

            if(Build.VERSION.SDK_INT < 28){
                // 안드로이드 9.0 (PIE) 버전 보다 낮을 경우

                bitmap = MediaStore.Images.Media.getBitmap(contentResolver,Uri.fromFile(file))

                image.setImageBitmap(bitmap)
            }else {
                //pie 버번 보다 이상 일 경우

                val decode = ImageDecoder.createSource(
                        this.contentResolver,
                        Uri.fromFile(file)
                )
                bitmap = ImageDecoder.decodeBitmap(decode)
                image.setImageBitmap(bitmap)
            }
            savePhoto(bitmap)
        }

    }

    private fun savePhoto(bitmap: Bitmap) {
//        val folderPath = Environment.getExternalStorageDirectory().absolutePath + "/" // 사진 폴더로 저장하기 위한 경로 선언
        val folderPath = this.getExternalFilesDir(Environment.DIRECTORY_PICTURES)?.absolutePath + "/Pictures/" // 사진 폴더로 저장하기 위한 경로 선언
        val timestamp : String  = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val fileName = "${timestamp}.jpeg"
        val folder = File(folderPath)

        if(!folder.isDirectory) {
        // 현재 해당 경로에 폴더가 존재하는 지 않는다면
            folder.mkdir() // makedirectory 의 줄임말 해당 경로에 폴더 자동으로 새로 만들기
        }
        // 실제적인 저장처리
            println(folderPath)
            println(fileName)
        val out = FileOutputStream(folderPath+fileName)
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,out)

        Toast.makeText(this,"사진이 앨범에 저장되었습니다",Toast.LENGTH_SHORT).show()

    }
}