package com.rajat.pdfviewer

import android.graphics.Bitmap
import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.LinearInterpolator
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.RecyclerView
import com.rajat.pdfviewer.databinding.ListItemPdfPageBinding
import com.rajat.pdfviewer.util.hide
import com.rajat.pdfviewer.util.show

/**
 * Created by Rajat on 11,July,2020
 */

internal class PdfViewAdapter(
    private val renderer: PdfRendererCore,
    private val pageSpacing: Rect,
    private val enableLoadingForPages: Boolean
) :
    RecyclerView.Adapter<PdfViewAdapter.PdfPageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PdfPageViewHolder {
        return PdfPageViewHolder(
            ListItemPdfPageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return renderer.getPageCount()
    }

    override fun onBindViewHolder(holder: PdfPageViewHolder, position: Int) {
        holder.bind(position)
    }

    inner class PdfPageViewHolder(itemView: ListItemPdfPageBinding) : RecyclerView.ViewHolder(itemView.root) {
        fun bind(position: Int) {
            with(itemView) {
                handleLoadingForPage(this, position)

                val pageView: ImageView = findViewById(R.id.pageView);
                val containerView: FrameLayout = findViewById(R.id.container_view);
                val loadingProgress: FrameLayout = findViewById(R.id.loadingLayoutInclude);

                pageView.setImageBitmap(null)
                renderer.renderPage(position) { bitmap: Bitmap?, pageNo: Int ->
                    if (pageNo != position)
                        return@renderPage
                    bitmap?.let {
                        containerView.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                            height =
                                (containerView.width.toFloat() / ((bitmap.width.toFloat() / bitmap.height.toFloat()))).toInt()
                            this.topMargin = pageSpacing.top
                            this.leftMargin = pageSpacing.left
                            this.rightMargin = pageSpacing.right
                            this.bottomMargin = pageSpacing.bottom
                        }
                        pageView.setImageBitmap(bitmap)
                        pageView.animation = AlphaAnimation(0F, 1F).apply {
                            interpolator = LinearInterpolator()
                            duration = 300
                        }

                        loadingProgress.hide()
                    }
                }
            }
        }

        private fun handleLoadingForPage(page: View, position: Int) {
            val loadingProgress: FrameLayout = page.findViewById(R.id.loadingLayoutInclude);

            if (!enableLoadingForPages) {
                loadingProgress.hide()
                return
            }

            if (renderer.pageExistInCache(position)) {
                loadingProgress.hide()
            } else {
                loadingProgress.show()
            }
        }
    }
}