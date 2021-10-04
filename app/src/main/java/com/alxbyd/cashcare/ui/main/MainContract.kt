package com.alxbyd.cashcare.ui.main

import com.alxbyd.cashcare.ui.basemvp.BaseContract

interface MainContract {

    interface Presenter : BaseContract.Presenter<View> {
        fun onFirstAttach()
        fun handleClickOnTransactions()
        fun handleClickOnReport()
    }

    interface View : BaseContract.View

    interface Router : BaseContract.Router {
        fun showTransactionsScreen()
        fun showReportsScreen()
    }

}