package com.alxbyd.cashcare.ui.transactions.presenter

import com.alxbyd.cashcare.repositories.TransactionsRepositoryImpl
import com.alxbyd.cashcare.ui.basemvp.BasePresenter
import com.alxbyd.cashcare.ui.transactions.TransactionsContract
import com.alxbyd.cashcare.utils.dataclasses.Transaction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class TransactionsPresenter(
    private val router: TransactionsRouter,
    private val transactionsRepository: TransactionsRepositoryImpl,
) : TransactionsContract.Presenter,
    BasePresenter<TransactionsContract.View>() {

    override fun editTransaction(idTransaction: Int) {
        router.showAddEditTransactionScreen(idTransaction)
    }

    override fun handleClickOnAddTransaction() {
        router.showAddEditTransactionScreen(null)
    }

    override fun handleClickOnCategories() {
        router.showCategoriesScreen()
    }

    override fun handleClickOnNotification() {
//        TODO("Not yet implemented")
//         click on enabled/disabled
//         notification in toolbar menu
    }

    override fun onFirstAttach() {
        view?.showProgress(true)
        presenterScope?.launch {
            transactionsRepository.balanceStateFlow.collect { balance ->
                view?.showAccountBalance(balance)
                view?.showProgress(false)
            }
        }
        //Need to change dispatcher to Main, but recyclerHeader not works.
        //Fix header and change dispatcher
        presenterScope?.launch(Dispatchers.IO) {
            transactionsRepository.transactionsStateFlow.collect { transactions ->
                view?.showTransactions(transactions.sortedByDescending { it.date })
            }
        }
    }

    override fun handleClickOnExport() {
//        TODO("Not yet implemented")
//         click on go to export screen in toolbar menu
    }

    override fun handleClickOnExit() {
        router.exit()
    }

}