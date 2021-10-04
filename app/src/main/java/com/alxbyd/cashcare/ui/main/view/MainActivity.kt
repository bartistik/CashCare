package com.alxbyd.cashcare.ui.main.view

import android.os.Bundle
import com.alxbyd.cashcare.R
import com.alxbyd.cashcare.ui.basemvp.BaseActivity
import com.alxbyd.cashcare.ui.main.MainContract
import com.alxbyd.cashcare.ui.main.presenter.MainPresenter
import com.alxbyd.cashcare.ui.main.presenter.MainRouter
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : MainContract.View,
    BaseActivity<MainContract.View, MainContract.Presenter>(R.layout.activity_main) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter.onFirstAttach()
        initBottomNavigationListener()
    }

    private fun initBottomNavigationListener() {
        findViewById<BottomNavigationView>(R.id.bottom_navigation)
            .setOnItemSelectedListener { item ->
                return@setOnItemSelectedListener when (item.itemId) {
                    R.id.transaction_view -> {
                        presenter.handleClickOnTransactions()
                        true
                    }
                    R.id.reports_view -> {
                        presenter.handleClickOnReport()
                        true
                    }
                    else -> false
                }
            }
    }

    override fun createPresenter(): MainContract.Presenter {
        val router = MainRouter(this)
        return MainPresenter(router)
    }

}