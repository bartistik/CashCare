package com.alxbyd.cashcare.ui.categories

import com.alxbyd.cashcare.ui.basemvp.BaseContract
import com.alxbyd.cashcare.utils.dataclasses.Category

interface CategoriesContract {

    interface Presenter : BaseContract.Presenter<View> {
        fun loadCategories()
        fun editCategory(category: Category)
        fun saveCategory(category: Category)
        fun deleteCategory(category: Category)
    }

    interface View : BaseContract.View {
        fun showCategories(categories: List<Category>)
        fun showValidateError()
    }

    interface Router : BaseContract.Router

}