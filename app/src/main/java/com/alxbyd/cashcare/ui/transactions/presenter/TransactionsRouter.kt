package com.alxbyd.cashcare.ui.transactions.presenter

import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import com.alxbyd.cashcare.R
import com.alxbyd.cashcare.ui.ComingSoonFragment
import com.alxbyd.cashcare.ui.addedittransaction.view.AddEditTransactionFragment
import com.alxbyd.cashcare.ui.transactions.TransactionsContract
import com.alxbyd.cashcare.ui.transactions.TransactionsContract
import com.alxbyd.cashcare.utils.dataclasses.Transaction

class TransactionsRouter(
    private val supportFragmentManager: FragmentManager,
) : TransactionsContract.Router {

    override fun showAddEditTransactionScreen(idTransaction: Int?) {
        supportFragmentManager.commit {
            setCustomAnimations(
                R.anim.slide_in,
                R.anim.fade_out,
                R.anim.fade_in,
                R.anim.slide_out
            )
            replace(
                R.id.fragment_container,
                AddEditTransactionFragment.newInstance(idTransaction)
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
