package com.alxbyd.cashcare.ui.main

import com.alxbyd.cashcare.ui.basemvp.BasePresenter

class MainPresenter(
    private val router: MainRouter
) : MainContract.Presenter, BasePresenter<MainContract.View>() {

    override fun onFirstAttach() {
        router.showTransactionsScreen()
    }

    override fun handleClickOnExit() {
        router.exit()
    }

    override fun handleClickOnTransactions() {
        router.showTransactionsScreen()
    }

    override fun handleClickOnReport() {
        router.showReportsScreen()
    }

}