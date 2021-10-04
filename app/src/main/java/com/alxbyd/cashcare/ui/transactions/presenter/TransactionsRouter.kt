package com.alxbyd.cashcare.ui.transactions.presenter

import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import com.alxbyd.cashcare.R
import com.alxbyd.cashcare.ui.ComingSoonFragment
import com.alxbyd.cashcare.ui.transactions.TransactionsContract
import com.alxbyd.cashcare.utils.dataclasses.Transaction

class TransactionsRouter(
    private val supportFragmentManager: FragmentManager,
) : TransactionsContract.Router {

    override fun showAddEditTransactionScreen(transaction: Transaction?) {
        supportFragmentManager.commit {
            replace(
                R.id.fragment_container,
                ComingSoonFragment.newInstance("Add transaction screen")
            ).addToBackStack(null)
        }
    }

    override fun showCategoriesScreen() {
        supportFragmentManager.commit {
            replace(
                R.id.fragment_container,
                ComingSoonFragment.newInstance("Categories Screen")
            ).addToBackStack(null)
        }
    }

    override fun exit() {
        supportFragmentManager.popBackStack()
    }

}
