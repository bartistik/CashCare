package com.alxbyd.cashcare.ui.transactions

import com.alxbyd.cashcare.ui.basemvp.BaseContract
import com.alxbyd.cashcare.utils.dataclasses.AccountBalance
import com.alxbyd.cashcare.utils.dataclasses.Transaction

interface TransactionsContract {

    interface Presenter : BaseContract.Presenter<View> {
        fun onFirstAttach()
        fun editTransaction(transaction: Transaction)
        fun handleClickOnAddTransaction()
        fun handleClickOnCategories()
        fun handleClickOnNotification()
        fun handleClickOnExport()
    }

    interface View : BaseContract.View {
        fun showAccountBalance(accountBalance: AccountBalance)
        fun showTransactions(transactions: List<Transaction>)
    }

    interface Router : BaseContract.Router {
        fun showAddEditTransactionScreen(transaction: Transaction?)
        fun showCategoriesScreen()
    }

}