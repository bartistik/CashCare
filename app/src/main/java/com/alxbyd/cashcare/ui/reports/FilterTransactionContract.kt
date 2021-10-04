package com.alxbyd.cashcare.ui.reports

import com.alxbyd.cashcare.ui.basemvp.BaseContract
import com.alxbyd.cashcare.utils.dataclasses.Category
import com.alxbyd.cashcare.utils.dataclasses.FilterValue

interface FilterTransactionContract {

    interface Presenter : BaseContract.Presenter<View> {
        fun loadCategories()
        fun createFilteredReport(filterValue: FilterValue)
    }

    interface View : BaseContract.View {
        fun showCategories(categories: List<Category>)
    }

    interface Router : BaseContract.Router {
        fun showFilteredReportScreen()
    }

}