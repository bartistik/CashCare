package com.alxbyd.cashcare.ui.addedittransaction.view

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import androidx.core.os.bundleOf
import androidx.core.view.ViewCompat.requireViewById
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alxbyd.cashcare.R
import com.alxbyd.cashcare.repositories.CategoriesRepositoryImpl
import com.alxbyd.cashcare.repositories.TransactionsRepositoryImpl
import com.alxbyd.cashcare.ui.addedittransaction.AddEditTransactionContract
import com.alxbyd.cashcare.ui.addedittransaction.adapter.CategoriesGridAdapter
import com.alxbyd.cashcare.ui.addedittransaction.adapter.CategoriesGridItemDecorator
import com.alxbyd.cashcare.ui.addedittransaction.presenter.AddEditTransactionPresenter
import com.alxbyd.cashcare.ui.addedittransaction.presenter.AddEditTransactionRouter
import com.alxbyd.cashcare.ui.basemvp.BaseFragment
import com.alxbyd.cashcare.ui.dialogs.CancelDeleteDialogFragment
import com.alxbyd.cashcare.ui.dialogs.OnDialogFragmentClickListener
import com.alxbyd.cashcare.utils.Ext.showSnackBar
import com.alxbyd.cashcare.utils.Ext.toFormatCurrentTransactionAmount
import com.alxbyd.cashcare.utils.Ext.toFormatDate
import com.alxbyd.cashcare.utils.dataclasses.Category
import com.alxbyd.cashcare.utils.dataclasses.Transaction
import com.alxbyd.cashcare.utils.dataclasses.TransactionType
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.util.*

class AddEditTransactionFragment :
    BaseFragment<AddEditTransactionContract.View, AddEditTransactionContract.Presenter>
        (R.layout.fragment_add_edit_transaction),
    AddEditTransactionContract.View,
    OnDialogFragmentClickListener {

    private lateinit var progressBar: ProgressBar
    private lateinit var amountEditTextLayout: TextInputLayout
    private lateinit var amountEditText: TextInputEditText
    private lateinit var commentEditText: TextInputEditText
    private lateinit var incomeButton: Button
    private lateinit var expenseButton: Button
    private lateinit var dateEditText: TextInputEditText
    private lateinit var categoriesRecyclerView: RecyclerView
    private lateinit var categoriesGridAdapter: CategoriesGridAdapter
    private var isTransactionExist = false
    private var transactionAmount: Double = 0.0
    private lateinit var transactionCategory: Category
    private lateinit var transactionDate: Date
    private lateinit var transactionType: TransactionType
    private var transactionComment: String = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewElements()
        initCategoriesRecyclerView()
        presenter.onFirstAttach()
        initToolbar()
        initClickListeners()
    }

    override fun createPresenter(): AddEditTransactionContract.Presenter {
        val router = AddEditTransactionRouter(parentFragmentManager)
        return AddEditTransactionPresenter(
            router,
            requireArguments().getInt("current_transaction"),
            TransactionsRepositoryImpl,
            CategoriesRepositoryImpl
        )
    }

    private fun initCategoriesRecyclerView() {
        categoriesRecyclerView.layoutManager = GridLayoutManager(requireContext(), 3)
        categoriesRecyclerView.addItemDecoration(CategoriesGridItemDecorator(18, 3))
        categoriesRecyclerView.itemAnimator = null
        categoriesGridAdapter = CategoriesGridAdapter(::onCategoryClickListener)
        categoriesRecyclerView.adapter = categoriesGridAdapter
    }

    private fun initToolbar() {
        toolbar.title = when (isTransactionExist) {
            true -> getString(R.string.edit_transaction)
            false -> getString(R.string.add_transaction)
        }
        actionBar.setDisplayHomeAsUpEnabled(true)
        actionBar.setHomeAsUpIndicator(R.drawable.ic_action_close)
        setHasOptionsMenu(true)
        toolbar.setNavigationOnClickListener {
            if (!isTransactionExist
                && (!amountEditText.text.isNullOrEmpty()
                        || !commentEditText.text.isNullOrEmpty()
                        || !dateEditText.text.isNullOrEmpty())
            ) {
                showCancelConfirmationDialog()
            } else {
                presenter.handleClickOnExit()
            }
        }
    }

    private fun initViewElements() {
        progressBar = requireViewById(requireView(), R.id.progressBar)
        amountEditTextLayout = requireViewById(requireView(), R.id.amount_text_input_layout)
        amountEditText = requireViewById(requireView(), R.id.amount_text_input)
        incomeButton = requireViewById(requireView(), R.id.income_button)
        expenseButton = requireViewById(requireView(), R.id.expense_button)
        dateEditText = requireViewById(requireView(), R.id.date_text_input)
        categoriesRecyclerView = requireViewById(requireView(), R.id.categories_rv)
        commentEditText = requireViewById(requireView(), R.id.comment_text_input)
    }

    private fun initClickListeners() {
        incomeButton.setOnClickListener {
            transactionType = TransactionType.INCOME
            setActiveTransactionType()
        }
        expenseButton.setOnClickListener {
            transactionType = TransactionType.EXPENSE
            setActiveTransactionType()
        }
        dateEditText.setOnClickListener {
            showDatePickerDialog()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_edit_transaction_menu, menu)
        if (!isTransactionExist) menu.removeItem(R.id.delete_transaction_btn)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.save_transaction_btn -> {
                if (
                    validateAmount() &&
                    validateCategory() &&
                    validateTransactionType() &&
                    validateComment()
                ) {
                    presenter.saveTransaction(
                        Transaction(
                            null,
                            transactionAmount,
                            transactionType,
                            transactionDate,
                            transactionCategory,
                            transactionComment
                        )
                    )
                }
                true
            }
            R.id.delete_transaction_btn -> {
                showDeleteConfirmationDialog()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun populateTransactionData(transaction: Transaction) {
        transactionAmount = transaction.amount
        transactionType = transaction.type
        transactionDate = transaction.date
        transactionCategory = transaction.category
        transactionComment = transaction.comment
        val positionTransactionCategory =
            categoriesGridAdapter.elementsList.indexOf(transaction.category)

        categoriesGridAdapter.swapElements(positionTransactionCategory)
        amountEditText.setText(transactionAmount.toFormatCurrentTransactionAmount())
        dateEditText.setText(transactionDate.toFormatDate())
        commentEditText.setText(transactionComment)
        setActiveTransactionType()
    }

    override fun showCategories(categories: List<Category>) {
        categoriesGridAdapter.submitList(categories)
    }

    private fun setActiveTransactionType() {
        when (transactionType) {
            TransactionType.INCOME -> {
                incomeButton.setBackgroundResource(R.drawable.btn_income_active)
                incomeButton.isEnabled = false
                expenseButton.setBackgroundResource(R.drawable.btn_expense_inactive)
                expenseButton.isEnabled = true
            }
            TransactionType.EXPENSE -> {
                incomeButton.setBackgroundResource(R.drawable.btn_income_inactive)
                incomeButton.isEnabled = true
                expenseButton.setBackgroundResource(R.drawable.btn_expense_active)
                expenseButton.isEnabled = false
            }
        }
    }

    private fun onCategoryClickListener(position: Int, category: Category) {
        transactionCategory = category
        categoriesGridAdapter.swapElements(position)
    }

    override fun validateAmount(): Boolean {
        val amountText = "${amountEditText.text?.trim()}"
        return when {
            amountText == "" -> {
                amountEditText.error = getString(R.string.please_enter_amount_error)
                false
            }
            amountText.length > 16 -> {
                amountEditText.error = getString(R.string.amount_too_long_error)
                false
            }
            else -> {
                try {
                    transactionAmount = amountText.toDouble()
                    true
                } catch (e: NumberFormatException) {
                    amountEditText.error = getString(R.string.please_enter_correct_amount_error)
                    false
                }
            }
        }
    }

    override fun validateComment(): Boolean {
        val transactionComment = commentEditText.text?.trim()

        return if (transactionComment!!.length > 300) {
            commentEditText.error = getString(R.string.comment_too_long_error)
            false
        } else {
            this.transactionComment = transactionComment.toString()
            return true
        }
    }

    override fun validateTransactionType(): Boolean {
        return if (!::transactionType.isInitialized) {
            requireView().showSnackBar(
                getString(R.string.set_transaction_type_error), Snackbar.LENGTH_SHORT
            )
            false
        } else {
            true
        }
    }

    override fun validateCategory(): Boolean {
        return if (!::transactionCategory.isInitialized) {
            requireView().showSnackBar(
                getString(R.string.set_category_error), Snackbar.LENGTH_SHORT
            )
            false
        } else {
            true
        }
    }

    override fun showProgress(shown: Boolean) {
        progressBar.visibility = if (shown) View.VISIBLE else View.GONE
    }

    override fun showError(error: Throwable) {
        requireView().showSnackBar(error.stackTraceToString(), Snackbar.LENGTH_SHORT)
        presenter.handleClickOnExit()
    }

    override fun addEditViewType(isDataAvailable: Boolean) {
        this.isTransactionExist = isDataAvailable
    }

    private fun showDatePickerDialog() {
        val transactionDate = Calendar.getInstance()
        val datePickerListener =
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                transactionDate.set(Calendar.YEAR, year)
                transactionDate.set(Calendar.MONTH, monthOfYear)
                transactionDate.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                dateEditText.setText(transactionDate.time.toFormatDate())
                this.transactionDate = transactionDate.time
            }
        DatePickerDialog(
            requireActivity(), R.style.DialogTheme, datePickerListener,
            transactionDate.get(Calendar.YEAR),
            transactionDate.get(Calendar.MONTH),
            transactionDate.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    override fun showDeleteConfirmationDialog() {
        val dialog = CancelDeleteDialogFragment.newInstance(
            getString(R.string.title_dialog_delete_transaction),
            getString(R.string.message_dialog_cancel_delete_transaction)
        )
        dialog.show(childFragmentManager, "dialog")
    }

    override fun showCancelConfirmationDialog() {
        val dialog = CancelDeleteDialogFragment.newInstance(
            getString(R.string.title_dialog_cancel_transaction),
            getString(R.string.message_dialog_cancel_delete_transaction)
        )
        dialog.show(childFragmentManager, "dialog")
    }

    override fun onOkClickedDialog(dialog: CancelDeleteDialogFragment) {
        if (isTransactionExist) presenter.deleteTransaction()
        presenter.handleClickOnExit()
    }

    companion object {
        private const val CURRENT_TRANSACTION = "current_transaction"
        fun newInstance(idTransaction: Int?) = AddEditTransactionFragment().apply {
            arguments = bundleOf(
                CURRENT_TRANSACTION to idTransaction
            )
        }
    }

}