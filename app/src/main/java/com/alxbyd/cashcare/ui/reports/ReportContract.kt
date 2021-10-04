package com.alxbyd.cashcare.ui.reports

import com.alxbyd.cashcare.ui.basemvp.BaseContract
import com.alxbyd.cashcare.utils.dataclasses.Transaction

interface ReportContract {

    interface Presenter : BaseContract.Presenter<View> {
        fun loadFilteredTransactions()
        fun exportTransactionsClicked()
    }

    interface View : BaseContract.View {
        fun showReport(filteredTransactions: List<Transaction>)
    }

    interface Router : BaseContract.Router

}