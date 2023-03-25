package com.rajat.pdfviewer

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.View.GONE
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.AppBarLayout
import com.rajat.pdfviewer.databinding.ActivityPdfViewerBinding
import java.io.File

/**
 * Created by Rajat on 11,July,2020
 */

class PdfViewerActivity : AppCompatActivity() {

    private var fileUrl: String? = null

    companion object {
        const val FILE_URL = "pdf_file_url"
        const val FILE_TITLE = "pdf_file_title"
        const val FROM_ASSETS = "from_assests"
        var isPDFFromPath = false
        var isFromAssets = false

        fun launchPdfFromPath(
            context: Context?,
            path: String?,
            pdfTitle: String?,
            fromAssets: Boolean = false
        ): Intent {
            val intent = Intent(context, PdfViewerActivity::class.java)
            intent.putExtra(FILE_URL, path)
            intent.putExtra(FILE_TITLE, pdfTitle)
            intent.putExtra(FROM_ASSETS, fromAssets)
            isPDFFromPath = true
            return intent
        }

    }

    private lateinit var binding: ActivityPdfViewerBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPdfViewerBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setUpToolbar(
            intent.extras!!.getString(
                FILE_TITLE,
                "PDF"
            )
        )

        isFromAssets = intent.extras!!.getBoolean(
            FROM_ASSETS,
            false
        )

        init()
    }

    private fun init() {
        if (intent.extras!!.containsKey(FILE_URL)) {
            fileUrl = intent.extras!!.getString(FILE_URL)
            if (isPDFFromPath) {
                initPdfViewerWithPath(this.fileUrl)
            } else {
                throw UnsupportedOperationException()
            }
        }
    }


    private fun setUpToolbar(toolbarTitle: String) {
        val toolbarInclude: AppBarLayout = findViewById(R.id.toolbarInclude);
        val toolbar: androidx.appcompat.widget.Toolbar = toolbarInclude.findViewById(R.id.toolbar);
        val tvAppBarTitle: TextView = toolbarInclude.findViewById(R.id.tvAppBarTitle);


        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            tvAppBarTitle.text = toolbarTitle
            setDisplayShowTitleEnabled(false)
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish() // close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initPdfViewerWithPath(filePath: String?) {
        if (TextUtils.isEmpty(filePath)) onPdfError()

        //Initiating PDf Viewer with URL
        try {

            val file = if (isFromAssets)
                com.rajat.pdfviewer.util.FileUtils.fileFromAsset(this, filePath!!)
            else
                File(filePath!!)


            binding.pdfView.initWithFile(
                file,
                PdfQuality.NORMAL
            )

        } catch (e: Exception) {
            onPdfError()
        }
    }

    private fun onPdfError() {
        Toast.makeText(this, "Pdf has been corrupted", Toast.LENGTH_SHORT).show()
        true.showProgressBar()
        finish()
    }

    private fun Boolean.showProgressBar() {
        binding.progressBar.visibility = if (this) View.VISIBLE else GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.pdfView.closePdfRender()
    }

}