package br.com.generics

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import br.com.generics.extensions.createBitmap
import kotlinx.android.synthetic.main.activity_result.*
import java.io.File
import java.io.FileOutputStream

class ResultActivity : AppCompatActivity() {

    private val REQUEST_WRITE_IMAGE = 123

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        val frase = intent.getStringExtra("frase")

        txt_frase.text = frase

        btn_shared.setOnClickListener {
            checkPermission()
        }
    }

    fun checkPermission(){
        val checkPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)

        if(checkPermission != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_WRITE_IMAGE)
        }
        else{
            createImageFromView()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(requestCode == REQUEST_WRITE_IMAGE){
            if (grantResults.get(0) == 0){
                createImageFromView()
            }
        }
    }

    fun createImageFromView(){
        val view = getWindow().getDecorView().findViewById<LinearLayout>(R.id.box)

        val bitmap = view.createBitmap()

        val file = File(getExternalCacheDir(), "${System.currentTimeMillis()}_frase.png")
        val fOut = FileOutputStream(file)
        bitmap?.compress(Bitmap.CompressFormat.PNG, 100, fOut)
        fOut.flush()
        fOut.close()
        file.setReadable(true, false)

        shared(file)
    }

    fun shared(file: File){
        val photoURI = FileProvider.getUriForFile(this,
            getString(R.string.file_provider_authority),
            file);

        val intent = Intent(Intent.ACTION_SEND);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(Intent.EXTRA_STREAM, photoURI);
        intent.setType("image/png");
        startActivity(Intent.createChooser(intent, getString(R.string.share)));
    }
}
