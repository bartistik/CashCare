package com.alxbyd.cashcare.ui.addedittransaction.presenter

import com.alxbyd.cashcare.repositories.CategoriesRepositoryImpl
import com.alxbyd.cashcare.repositories.TransactionsRepositoryImpl
import com.alxbyd.cashcare.ui.addedittransaction.AddEditTransactionContract
import com.alxbyd.cashcare.ui.basemvp.BasePresenter
import com.alxbyd.cashcare.utils.dataclasses.Transaction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class AddEditTransactionPresenter(
    private val router: AddEditTransactionRouter,
    private val idTransaction: Int?,
    private val transactionsRepositoryImpl: TransactionsRepositoryImpl,
    private val categoriesRepositoryImpl: CategoriesRepositoryImpl
) : BasePresenter<AddEditTransactionContract.View>(), AddEditTransactionContract.Presenter {

    override fun onFirstAttach() {
        view?.showProgress(true)

        if (idTransaction != null) {
            view?.addEditViewType(true)
            presenterScope?.launch {
                val transaction =
                    transactionsRepositoryImpl.transactionsStateFlow.value[idTransaction]
                view?.populateTransactionData(transaction)
            }
        } else {
            view?.addEditViewType(false)
        }
        presenterScope?.launch(Dispatchers.IO) {
            categoriesRepositoryImpl.getAllCategories().collect { categories ->
                view?.showCategories(categories)
                view?.showProgress(false)
            }
        }
    }

    override fun deleteTransaction() {
        presenterScope?.launch {
            transactionsRepositoryImpl.deleteTransaction(idTransaction!!)
        }
    }

    override fun saveTransaction(transaction: Transaction) {
        presenterScope?.launch {
            transactionsRepositoryImpl.saveTransaction(transaction)
        }
        handleClickOnExit()
    }

    override fun cancelChanges() {
        view?.showCancelConfirmationDialog()
    }

    override fun handleClickOnExit() {
        router.exit()
    }

}