package com.alxbyd.cashcare.ui.addedittransaction.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat.getColor
import androidx.core.view.ViewCompat.requireViewById
import com.alxbyd.cashcare.R
import com.alxbyd.cashcare.ui.basemvp.BaseAdapter
import com.alxbyd.cashcare.utils.dataclasses.Category
import java.util.*

class CategoriesGridAdapter(
    private val onCategoryClickListener: (Int, Category) -> Unit,
) : BaseAdapter<Category, CategoriesGridAdapter.CategoryViewHolder>() {

    class CategoryViewHolder(
        itemView: View,
        onCategoryClickListener: (Int, Category) -> Unit
    ) : BaseAdapter.BaseViewHolder<Category>(itemView) {

        private lateinit var currentCategory: Category
        private val categoryNameTv =
            requireViewById<AppCompatTextView>(itemView, R.id.category_name_tv)

        init {
            itemView.setOnClickListener {
                onCategoryClickListener(adapterPosition, currentCategory)
            }
        }

        override fun bind(element: Category) {
            this.currentCategory = element
            categoryNameTv.text = element.name
            if (itemViewType == 0) {
                categoryNameTv.setBackgroundColor(getColor(itemView.context, R.color.orange))
            }
        }
    }

    override fun areItemsTheSame(oldElement: Category, newElement: Category): Boolean {
        return oldElement.id == newElement.id
    }

    override fun areContentTheSame(oldElement: Category, newElement: Category): Boolean {
        return oldElement == newElement
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.holder_gv_category, parent, false)
        return CategoryViewHolder(view, onCategoryClickListener)
    }

    fun swapElements(currentPosition: Int, firstPosition: Int = 0) {
        Collections.swap(elementsList, currentPosition, firstPosition)
        notifyItemMoved(currentPosition, firstPosition)
    }

}