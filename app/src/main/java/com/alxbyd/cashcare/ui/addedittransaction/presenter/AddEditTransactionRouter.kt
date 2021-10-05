package com.alxbyd.cashcare.ui.addedittransaction.presenter

import androidx.fragment.app.FragmentManager
import com.alxbyd.cashcare.ui.addedittransaction.AddEditTransactionContract

class AddEditTransactionRouter(
    private val supportFragmentManager: FragmentManager,
) : AddEditTransactionContract.Router {

    override fun exit() {
        supportFragmentManager.popBackStack()
    }

}