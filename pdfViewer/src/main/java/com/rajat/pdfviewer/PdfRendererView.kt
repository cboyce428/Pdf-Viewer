package com.rajat.pdfviewer

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.NO_POSITION
import java.io.File

/**
 * Created by Rajat on 11,July,2020
 */

class PdfRendererView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private lateinit var recyclerView: RecyclerView
    private lateinit var pageNo: TextView
    private lateinit var pdfRendererCore: PdfRendererCore
    private lateinit var pdfViewAdapter: PdfViewAdapter
    private var quality = PdfQuality.NORMAL
    private var showDivider = true
    private var divider: Drawable? = null
    private var runnable = Runnable {}
    var enableLoadingForPages: Boolean = false

    private var pdfRendererCoreInitialised = false
    var pageMargin: Rect = Rect(0,0,0,0)
    var statusListener: StatusCallBack? = null

    val totalPageCount: Int
        get() {
            return pdfRendererCore.getPageCount()
        }

    interface StatusCallBack {
        fun onPageChanged(currentPage: Int, totalPage: Int) {}
    }

    fun initWithFile(file: File, pdfQuality: PdfQuality = this.quality) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
            throw UnsupportedOperationException("should be over API 21")
        init(file, pdfQuality)
    }

    private fun init(file: File, pdfQuality: PdfQuality) {
        pdfRendererCore = PdfRendererCore(context, file, pdfQuality)
        pdfRendererCoreInitialised = true
        pdfViewAdapter = PdfViewAdapter(pdfRendererCore, pageMargin, enableLoadingForPages)
        val v = LayoutInflater.from(context).inflate(R.layout.pdf_rendererview, this, false)
        addView(v)
        recyclerView = findViewById(R.id.recyclerView)
        pageNo = findViewById(R.id.pageNumber)
        recyclerView.apply {
            adapter = pdfViewAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            itemAnimator = DefaultItemAnimator()
            if (showDivider) {
                DividerItemDecoration(context, DividerItemDecoration.VERTICAL).apply {
                    divider?.let { setDrawable(it) }
                }.let { addItemDecoration(it) }
            }
            addOnScrollListener(scrollListener)
        }

        runnable = Runnable {
            pageNo.visibility = View.GONE
        }

    }

    private val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            (recyclerView.layoutManager as LinearLayoutManager).run {
                var foundPosition : Int = findFirstCompletelyVisibleItemPosition()

                pageNo.run {
                    if (foundPosition != NO_POSITION)
                        text = context.getString(R.string.pdfView_page_no,foundPosition + 1,totalPageCount)
                    pageNo.visibility = View.VISIBLE
                }

                if (foundPosition == 0)
                    pageNo.postDelayed({
                        pageNo.visibility = GONE
                    }, 3000)

                if (foundPosition != NO_POSITION) {
                    statusListener?.onPageChanged(foundPosition, totalPageCount)
                    return@run
                }
                foundPosition = findFirstVisibleItemPosition()
                if (foundPosition != NO_POSITION) {
                    statusListener?.onPageChanged(foundPosition, totalPageCount)
                    return@run
                }
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                pageNo.postDelayed(runnable, 3000)
            } else {
                pageNo.removeCallbacks(runnable)
            }
        }

    }

    init {
        getAttrs(attrs, defStyleAttr)
    }

    private fun getAttrs(attrs: AttributeSet?, defStyle: Int) {
        val typedArray =
            context.obtainStyledAttributes(attrs, R.styleable.PdfRendererView, defStyle, 0)
        setTypeArray(typedArray)
    }

    private fun setTypeArray(typedArray: TypedArray) {
        val ratio =
            typedArray.getInt(R.styleable.PdfRendererView_pdfView_quality, PdfQuality.NORMAL.ratio)
        quality = PdfQuality.values().first { it.ratio == ratio }
        showDivider = typedArray.getBoolean(R.styleable.PdfRendererView_pdfView_showDivider, true)
        divider = typedArray.getDrawable(R.styleable.PdfRendererView_pdfView_divider)
        enableLoadingForPages = typedArray.getBoolean(R.styleable.PdfRendererView_pdfView_enableLoadingForPages, enableLoadingForPages)

        val marginDim = typedArray.getDimensionPixelSize(R.styleable.PdfRendererView_pdfView_page_margin, 0)
        pageMargin = Rect(marginDim, marginDim, marginDim, marginDim).apply {
            top = typedArray.getDimensionPixelSize(R.styleable.PdfRendererView_pdfView_page_marginTop, top)
            left = typedArray.getDimensionPixelSize(R.styleable.PdfRendererView_pdfView_page_marginLeft, left)
            right = typedArray.getDimensionPixelSize(R.styleable.PdfRendererView_pdfView_page_marginRight, right)
            bottom = typedArray.getDimensionPixelSize(R.styleable.PdfRendererView_pdfView_page_marginBottom, bottom)
        }

        typedArray.recycle()
    }

    fun closePdfRender() {
        if (pdfRendererCoreInitialised)
            pdfRendererCore.closePdfRender()
    }
}