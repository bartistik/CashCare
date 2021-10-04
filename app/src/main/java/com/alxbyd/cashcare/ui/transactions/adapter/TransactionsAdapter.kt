package com.alxbyd.cashcare.ui.transactions.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat.getColor
import androidx.core.view.ViewCompat.requireViewById
import com.alxbyd.cashcare.R
import com.alxbyd.cashcare.ui.basemvp.BaseAdapter
import com.alxbyd.cashcare.utils.Ext.toFormatAmountTransactions
import com.alxbyd.cashcare.utils.dataclasses.Transaction
import com.alxbyd.cashcare.utils.dataclasses.TransactionType

class TransactionsAdapter(
    private val onTransactionClickListener: (Transaction) -> Unit,
) : BaseAdapter<Transaction, TransactionsAdapter.TransactionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.holder_transaction, parent, false)

        return TransactionViewHolder(view, onTransactionClickListener)
    }

    class TransactionViewHolder(
        itemView: View,
        onTransactionClickListener: (Transaction) -> Unit,
    ) : BaseAdapter.BaseViewHolder<Transaction>(itemView) {

        private lateinit var currentTransaction: Transaction

        private val categoryTv = requireViewById<AppCompatTextView>(itemView, R.id.category_Tv)
        private val amountTv = requireViewById<AppCompatTextView>(itemView, R.id.amount_Tv)

        init {
            itemView.setOnClickListener {
                onTransactionClickListener(currentTransaction)
            }
        }

        override fun bind(element: Transaction) {
            this.currentTransaction = element
            categoryTv.text = element.category.name
            setAmount(element.type, element.amount)

            val colorView = getColor(
                itemView.context,
                if (element.type == TransactionType.EXPENSE) {
                    R.color.red_transparent_expense
                } else R.color.green_transparent_income
            )

            itemView.setBackgroundColor(colorView)
        }

        private fun setAmount(transactionType: TransactionType, amount: Double) {
            val colorTv = getColor(
                itemView.context,
                if (transactionType == TransactionType.EXPENSE) {
                    R.color.red_expense
                } else R.color.green_income
            )

            val sign: Char = if (transactionType == TransactionType.EXPENSE) '-' else '+'

            amountTv.text = amount.toFormatAmountTransactions(sign)
            amountTv.setTextColor(colorTv)
            categoryTv.setTextColor(colorTv)
        }

    }

    override fun areItemsTheSame(oldElement: Transaction, newElement: Transaction): Boolean {
        return oldElement.id == newElement.id
    }

    override fun areContentTheSame(oldElement: Transaction, newElement: Transaction): Boolean {
        return oldElement == newElement
    }

}
