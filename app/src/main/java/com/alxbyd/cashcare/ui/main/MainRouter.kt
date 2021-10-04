package com.alxbyd.cashcare.ui.main

import androidx.appcompat.app.AppCompatActivity
import com.alxbyd.cashcare.R
import com.alxbyd.cashcare.ui.ComingSoonFragment
import com.alxbyd.cashcare.ui.basemvp.BaseRouter

class MainRouter(
    private val activity: AppCompatActivity
) : BaseRouter(activity), MainContract.Router {

    override fun showTransactionsScreen() {
        activity.supportFragmentManager.beginTransaction()
            .replace(
                R.id.fragment_container,
                ComingSoonFragment.newInstance(activity.resources.getString(R.string.transactions)),
                activity.getString(R.string.transactions_screen_tag)
            )
            .commit()
        return
    }

    override fun showReportsScreen() {
        activity.supportFragmentManager.beginTransaction()
            .addToBackStack(null)
            .replace(
                R.id.fragment_container,
                ComingSoonFragment.newInstance(activity.resources.getString(R.string.reports)),
                activity.getString(R.string.reports_screen_tag)
            )
            .commit()
        return
    }

    override fun exit() {
        activity.finish()
    }

}