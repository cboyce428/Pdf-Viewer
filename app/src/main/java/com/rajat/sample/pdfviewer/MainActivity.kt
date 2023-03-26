package com.rajat.sample.pdfviewer

import android.content.Context
import android.content.res.AssetManager
import android.os.Bundle
import android.os.Environment
import androidx.appcompat.app.AppCompatActivity
import com.rajat.pdfviewer.PdfViewerActivity
import com.rajat.sample.pdfviewer.databinding.ActivityMainBinding
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.openPdfFile.setOnClickListener {
            // For this demo copy asset to a file
            val saveAs = File(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "test.pdf").absolutePath;
            copyAssetToFile(this, "quote.pdf", saveAs)
            launchPdfFromFile(saveAs);
        }

        binding.openPdfAsset.setOnClickListener {
            launchPdfFromAsset()
        }
    }

    private fun copyAssetToFile(context: Context, assetFile : String, saveAs : String) {
        val assetManager: AssetManager = context.getAssets()
        try {
            val `in`: InputStream = assetManager.open(assetFile)
            val out: OutputStream = FileOutputStream(saveAs)
            val buffer = ByteArray(1024)
            var read: Int = `in`.read(buffer)
            while (read != -1) {
                out.write(buffer, 0, read)
                read = `in`.read(buffer)
            }
        } catch (e: Exception) {
            e.message
        }
    }

    private fun launchPdfFromAsset() {
        startActivity(
            PdfViewerActivity.Companion.launchPdfFromPath(
                this,
                "singlepage.pdf",
                "Asset Test",
                true
            )
        )
    }

    private fun launchPdfFromFile(filename : String) {
        startActivity(
            PdfViewerActivity.Companion.launchPdfFromPath(
                this,
                filename,
                "File Test",
                false
            )
        )
    }
}