package com.alxbyd.cashcare.ui.basemvp

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class BaseAdapter<E, VH> : RecyclerView.Adapter<VH>()
        where  VH : BaseAdapter.BaseViewHolder<E> {

    lateinit var elementsList: List<E>

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(elementsList[position])
    }

    fun submitList(newElementList: List<E>) {

        when {
            !this::elementsList.isInitialized -> {
                elementsList = newElementList
                return
            }
            newElementList.isEmpty() -> {
                notifyItemRangeRemoved(0, elementsList.size)
            }
            elementsList.isEmpty() -> {
                notifyItemRangeInserted(0, newElementList.size)
            }
            else -> {
                notifyItemsChanged(elementsList, newElementList)
            }
        }
        elementsList = newElementList
    }

    private fun notifyItemsChanged(oldElementsList: List<E>, newElementList: List<E>) {
        val oldList = oldElementsList.toArrayList()
        val newList = newElementList.toArrayList()

        if (oldList.size > newList.size) {
            notifyItemRangeRemoved(newList.size, oldList.size - newList.size)
        }

        for ((newElementIndex, newElement) in newList.withIndex()) {

            for ((oldElementIndex, oldElement) in oldList.withIndex()) {

                if (areItemsTheSame(oldElement, newElement)) {
                    if (areContentTheSame(oldElement, newElement)) {
                        if (newElementIndex != oldElementIndex) {
                            notifyItemChanged(newElementIndex)
                        }
                    }
                } else if (oldElementIndex == oldList.size - 1) {
                    if (newElementIndex > oldElementIndex) {
                        notifyItemInserted(newElementIndex)
                    } else {
                        notifyItemChanged(oldElementIndex)
                    }
                } else {
                    notifyItemChanged(newElementIndex)
                }

            }

        }
    }

    abstract fun areItemsTheSame(oldElement: E, newElement: E): Boolean

    abstract fun areContentTheSame(oldElement: E, newElement: E): Boolean

    private fun List<E>.toArrayList(): ArrayList<E> {
        return ArrayList(this)
    }

    override fun getItemCount(): Int {
        return elementsList.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    abstract class BaseViewHolder<E>(itemView: View) : RecyclerView.ViewHolder(itemView) {

        abstract fun bind(element: E)

    }

}