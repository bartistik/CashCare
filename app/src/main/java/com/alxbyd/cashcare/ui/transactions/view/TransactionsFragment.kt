package com.alxbyd.cashcare.ui.transactions.view

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.view.ViewCompat.requireViewById
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alxbyd.cashcare.R
import com.alxbyd.cashcare.repositories.TransactionsRepositoryImpl
import com.alxbyd.cashcare.ui.basemvp.BaseFragment
import com.alxbyd.cashcare.ui.transactions.TransactionsContract
import com.alxbyd.cashcare.ui.transactions.adapter.TransactionsAdapter
import com.alxbyd.cashcare.ui.transactions.adapter.TransactionsItemDecorator
import com.alxbyd.cashcare.ui.transactions.adapter.TransactionsItemDecorator.SectionCallback
import com.alxbyd.cashcare.ui.transactions.presenter.TransactionsPresenter
import com.alxbyd.cashcare.ui.transactions.presenter.TransactionsRouter
import com.alxbyd.cashcare.utils.Ext.showSnackBar
import com.alxbyd.cashcare.utils.Ext.toFormatBalanceAmount
import com.alxbyd.cashcare.utils.Ext.toFormatDate
import com.alxbyd.cashcare.utils.dataclasses.AccountBalance
import com.alxbyd.cashcare.utils.dataclasses.Transaction
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import java.util.*

class TransactionsFragment :
    BaseFragment<TransactionsContract.View, TransactionsContract.Presenter>
        (R.layout.fragment_transactions), TransactionsContract.View {

    private lateinit var recyclerView: RecyclerView
    private lateinit var balanceTextView: AppCompatTextView
    private lateinit var floatingActionButton: FloatingActionButton
    private lateinit var progressBar: ProgressBar
    private lateinit var transactionAdapter: TransactionsAdapter
    private lateinit var transactionsItemDecorator: TransactionsItemDecorator

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewElements()
        initToolbar()
        initTransactionsRecyclerView()
        setFloatingActionButtonListener()
        presenter.onFirstAttach()
    }

    override fun createPresenter(): TransactionsPresenter {
        val router = TransactionsRouter(parentFragmentManager)
        val repositoryList = TransactionsRepositoryImpl
        return TransactionsPresenter(router, repositoryList)
    }

    private fun initViewElements() {
        floatingActionButton = requireViewById(requireView(), R.id.floating_action_button)
        recyclerView = requireViewById(requireView(), R.id.transaction_rv)
        progressBar = requireViewById(requireView(), R.id.progressBar)
        balanceTextView = requireViewById(requireView(), R.id.balance_tV)
        transactionAdapter = TransactionsAdapter(::onTransactionClickListener)
    }

    private fun initToolbar() {
        toolbar.title = getString(R.string.app_name)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_transactions_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.categories_screen -> {
                presenter.handleClickOnCategories()
                true
            }
            R.id.notification -> {
                presenter.handleClickOnNotification()
                true
            }
            R.id.export_screen -> {
                presenter.handleClickOnExport()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun showAccountBalance(accountBalance: AccountBalance) {
        balanceTextView.visibility = View.VISIBLE
        balanceTextView.text = accountBalance.balance.toFormatBalanceAmount()
    }

    private fun initTransactionsRecyclerView() {
        recyclerView.layoutManager =
            LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = transactionAdapter
    }

    private fun setFloatingActionButtonListener() {
        floatingActionButton.setOnClickListener {
            presenter.handleClickOnAddTransaction()
        }
    }

    private fun onTransactionClickListener(transaction: Transaction) {
        presenter.editTransaction(transaction.id!!)
    }

    override fun showTransactions(transactions: List<Transaction>) {
        (recyclerView.adapter as TransactionsAdapter).submitList(transactions)

        transactionsItemDecorator = TransactionsItemDecorator(
            resources.getDimensionPixelSize(R.dimen.recycler_section_header_height),
            resources.getDimensionPixelSize(R.dimen.recycler_item_margin),
            true,
            getSectionCallBack(transactionAdapter.elementsList)
        )
        recyclerView.addItemDecoration(transactionsItemDecorator)
    }

    private fun getSectionCallBack(transactions: List<Transaction>): SectionCallback {
        return object : SectionCallback {
            override fun isSection(position: Int): Boolean {
                if (position == 0) return true
                if (position >= transactions.lastIndex) return false
                val currentTransactionDate = Calendar.getInstance()
                currentTransactionDate.time = transactions[position].date
                val previousTransactionDate = Calendar.getInstance()
                previousTransactionDate.time = transactions[position - 1].date
                return currentTransactionDate.get(Calendar.DATE) != previousTransactionDate.get(
                    Calendar.DATE
                )
            }

            override fun getSectionHeader(position: Int): String {
                return transactions[position].date.toFormatDate()
            }
        }
    }

    override fun showProgress(shown: Boolean) {
        progressBar.visibility = if (!shown) View.GONE else View.VISIBLE
        recyclerView.visibility = if (shown) View.GONE else View.VISIBLE
    }

    override fun showError(error: Throwable) {
        requireView().showSnackBar("${error.stackTrace}", Snackbar.LENGTH_SHORT)
    }

}