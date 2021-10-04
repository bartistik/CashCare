package com.alxbyd.cashcare.ui.addedittransaction

import com.alxbyd.cashcare.ui.basemvp.BaseContract
import com.alxbyd.cashcare.utils.dataclasses.Category
import com.alxbyd.cashcare.utils.dataclasses.Transaction

interface AddEditTransactionContract : BaseContract {

    interface Presenter : BaseContract.Presenter<View> {
        fun onFirstAttach()
        fun saveTransaction(transaction: Transaction)
        fun cancelChanges()
        fun loadTransactionData()
        fun deleteTransaction()
    }

    interface View : BaseContract.View {
        fun addEditViewType(isDataAvailable: Boolean)
        fun showCategories(categories: List<Category>)
        fun validateAmount(): Boolean
        fun validateTransactionType(): Boolean
        fun validateCategory(): Boolean
        fun validateComment(): Boolean
        fun populateTransactionData(transaction: Transaction)
        fun showDeleteConfirmationDialog()
        fun showCancelConfirmationDialog()
    }

    interface Router : BaseContract.Router

}