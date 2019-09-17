package br.com.generics

import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import androidx.core.content.FileProvider
import br.com.generics.extensions.createBitmap
import kotlinx.android.synthetic.main.activity_result.*
import java.io.File
import java.io.FileOutputStream

class ResultActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        val frase = intent.getStringExtra("frase")

        txt_frase.text = frase

        btn_shared.setOnClickListener {
            createImageFromView()
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
