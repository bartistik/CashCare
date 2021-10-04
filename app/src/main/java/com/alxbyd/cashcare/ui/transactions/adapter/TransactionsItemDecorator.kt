package com.alxbyd.cashcare.ui.transactions.adapter

import android.graphics.Canvas
import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.alxbyd.cashcare.R
import kotlin.math.max

class TransactionsItemDecorator(
    private val headerHeight: Int,
    private val marginItem: Int,
    private val sticky: Boolean,
    private var sectionCallback: SectionCallback
) : RecyclerView.ItemDecoration() {

    private lateinit var headerView: View
    private lateinit var header: TextView

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.bottom = marginItem
        val pos = parent.getChildAdapterPosition(view)
        if (
            sectionCallback.isSection(pos + 1)
            || parent.getChildAdapterPosition(view) == parent.adapter!!.itemCount - 1
        ) outRect.bottom = 0
        if (sectionCallback.isSection(pos)) outRect.top = headerHeight
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(c, parent, state)
        headerView = inflateHeaderView(parent)
        header = headerView.findViewById(R.id.header_item_section_tv)
        fixLayoutSize(headerView, parent)
        var previousHeader: CharSequence = ""

        for (i in 0 until parent.childCount) {
            val child = parent.getChildAt(i)
            val position = parent.getChildAdapterPosition(child)
            val title = sectionCallback.getSectionHeader(position)
            header.text = title

            if (previousHeader != title || sectionCallback.isSection(position)) {
                drawHeader(c, child, headerView)
                previousHeader = title
            }
        }
    }

    private fun drawHeader(c: Canvas, child: View, headerView: View) {
        c.save()

        when (sticky) {
            true -> c.translate(0F, max(0, child.top - headerView.height).toFloat())
            false -> c.translate(0F, (child.top - headerView.height).toFloat())
        }
        headerView.draw(c)
        c.restore()
    }

    private fun fixLayoutSize(view: View, parent: RecyclerView) {
        val widthSpec = View.MeasureSpec
            .makeMeasureSpec(parent.width, View.MeasureSpec.EXACTLY)
        val heightSpec = View.MeasureSpec
            .makeMeasureSpec(parent.height, View.MeasureSpec.UNSPECIFIED)
        val childWidth = ViewGroup.getChildMeasureSpec(
            widthSpec,
            parent.paddingLeft + parent.paddingRight,
            view.layoutParams.width
        )
        val childHeight = ViewGroup.getChildMeasureSpec(
            heightSpec,
            parent.paddingTop + parent.paddingBottom,
            view.layoutParams.height
        )

        view.measure(childWidth, childHeight)
        view.layout(0, 0, view.measuredWidth, view.measuredHeight)

    }

    private fun inflateHeaderView(parent: RecyclerView): View {
        return LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_header, parent, false)
    }

    interface SectionCallback {
        fun isSection(position: Int): Boolean
        fun getSectionHeader(position: Int): String
    }

}