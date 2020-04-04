package com.example.cloudstorage

import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat

class MainActivity : AppCompatActivity(), View.OnClickListener {

    var RC_STORAGE_PERMS1: Int = 101
    var RC_STORAGE_PERMS2: Int = 102
    var writeExStorage: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //init views
        findViewById<Button>(R.id.uploadBtn)?.setOnClickListener(this)
        findViewById<Button>(R.id.downloadBtn)?.setOnClickListener(this)
        findViewById<Button>(R.id.storageBtn)?.setOnClickListener(this)

    }

    //##REQUEST PERMISSIONS ##//
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            RC_STORAGE_PERMS1 -> {}
            RC_STORAGE_PERMS2 -> {
                if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    if(requestCode == RC_STORAGE_PERMS1){
                        startActivity(Intent(this, UploadActivity::class.java))
                    }else{
                        startActivity(Intent(this, DownloadActivity::class.java))
                    }
                }else{
                    //Create alert dialog//
                    val builder = AlertDialog.Builder(this)
                    builder.setMessage("Allow permissions")
                    builder.setPositiveButton(android.R.string.ok){dialog, which ->
                        dialog.dismiss()
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        intent.data = Uri.parse("package: $packageName")
                        startActivityForResult(intent, requestCode)
                    }
                    // Finally, make the alert dialog using builder
                    val dialog: AlertDialog = builder.create()

                    //cancel
                    dialog.setCancelable(false)

                    // Display the alert dialog on app interface
                    dialog.show()
                    //End Alert Dialog//
                }
            }
        }
    }
    //##END-->REQUEST PERMISSIONS ##//

    //##ACTIVITY RESULT ##/
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when(requestCode){
            RC_STORAGE_PERMS1 -> {}
            RC_STORAGE_PERMS2 -> {
                writeExStorage = ActivityCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                if(writeExStorage == PackageManager.PERMISSION_GRANTED){
                    if(requestCode == RC_STORAGE_PERMS1){
                        startActivity(Intent(this, UploadActivity::class.java))
                    }else{
                        startActivity(Intent(this, DownloadActivity::class.java))
                    }
                }else{
                    ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), RC_STORAGE_PERMS2)
                }
            }
        }
    }
    //##END--> ACTIVITY RESULT ##/

    override fun onClick(v: View?) {
       when(v?.id){
           R.id.uploadBtn -> {
               writeExStorage = ActivityCompat.checkSelfPermission(
                   this,
                   android.Manifest.permission.WRITE_EXTERNAL_STORAGE
               )
               if (writeExStorage == RC_STORAGE_PERMS1) {
                   startActivity(Intent(this, UploadActivity::class.java))
               } else {
                   startActivity(Intent(this, DownloadActivity::class.java))
               }
           }
           R.id.downloadBtn ->{
               writeExStorage = ActivityCompat.checkSelfPermission(
                   this,
                   android.Manifest.permission.WRITE_EXTERNAL_STORAGE
               )
               if (writeExStorage == RC_STORAGE_PERMS1) {
                   startActivity(Intent(this, DownloadActivity::class.java))
               } else {
                   ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), RC_STORAGE_PERMS2)
               }
           }
           R.id.storageBtn ->{
               startActivity(Intent(this, CSActivity::class.java))
           }
       }
    }
}
