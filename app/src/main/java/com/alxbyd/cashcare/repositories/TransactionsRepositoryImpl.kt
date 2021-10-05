package com.alxbyd.cashcare.repositories

import com.alxbyd.cashcare.utils.dataclasses.AccountBalance
import com.alxbyd.cashcare.utils.dataclasses.FilterValue
import com.alxbyd.cashcare.utils.dataclasses.Transaction
import com.alxbyd.cashcare.utils.dataclasses.TransactionType
import com.alxbyd.cashcare.utils.exceptions.NoSuchTransactionException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.*
import kotlin.random.Random

object TransactionsRepositoryImpl : TransactionRepository {

    private val categoriesRepository = CategoriesRepositoryImpl

    private val transactionsMutableStateFlow = MutableStateFlow(mutableListOf<Transaction>())
    val transactionsStateFlow: StateFlow<List<Transaction>> = transactionsMutableStateFlow

    private val balanceMutableStateFlow = MutableStateFlow(AccountBalance(0.0))
    val balanceStateFlow: StateFlow<AccountBalance> = balanceMutableStateFlow

    private fun updateBalance() {
        val accountBalance = AccountBalance(0.0)

        transactionsStateFlow.value.forEach {
            when (it.type) {
                TransactionType.INCOME -> accountBalance.balance += it.amount
                TransactionType.EXPENSE -> accountBalance.balance -= it.amount
            }
        }
        balanceMutableStateFlow.value = accountBalance
    }

    private suspend fun createMockTransactions(): MutableList<Transaction> {
        val mockTransactionsList = mutableListOf<Transaction>()
        for (t in 0..25) {
            mockTransactionsList.add(
                Transaction(
                    t,
                    Random.nextDouble(00.00, 9999.99),
                    TransactionType.values()[Random.nextInt(TransactionType.values().size)],
                    Date(Random.nextLong(34_626_545_000, 35_626_857_000)),
                    categoriesRepository.getAllCategories().value[Random.nextInt(0, 5)],
                    "mock comment"
                )
            )
        }
        return mockTransactionsList
    }

    init {
        CoroutineScope(Job() + Dispatchers.IO).launch {
            transactionsMutableStateFlow.value = createMockTransactions()
            updateBalance()
        }
    }

    override suspend fun addTransaction(transaction: Transaction) {
        val newTransaction = transaction.copy(
            id = transactionsMutableStateFlow.value.lastOrNull()?.id?.plus(1) ?: 1
        )
        val newTransactions = mutableListOf<Transaction>()

        newTransactions.addAll(transactionsMutableStateFlow.value)
        newTransactions.add(newTransaction)
        transactionsMutableStateFlow.value = newTransactions

        updateBalance()
    }

    override suspend fun saveTransaction(transaction: Transaction) {
        val indexTransaction =
            transactionsMutableStateFlow.value.indexOfFirst { it.id == transaction.id }
        if (indexTransaction == -1) {
            addTransaction(transaction)
        } else {
            val newTransactions = mutableListOf<Transaction>()

            newTransactions.addAll(transactionsMutableStateFlow.value)
            newTransactions[indexTransaction] = transaction
            transactionsMutableStateFlow.value = newTransactions

            updateBalance()
        }
    }

    @Throws(NoSuchTransactionException::class)
    override suspend fun deleteTransaction(idTransaction: Int) {
        val newTransactions = mutableListOf<Transaction>()
        val transaction =
            transactionsMutableStateFlow.value[idTransaction]

    override suspend fun deleteTransaction(transaction: Transaction) {
        val newTransactions = mutableListOf<Transaction>()

        newTransactions.addAll(transactionsMutableStateFlow.value)
        if (!newTransactions.remove(transaction)) throw NoSuchTransactionException()
        transactionsMutableStateFlow.value = newTransactions

        updateBalance()
    }

    override suspend fun getFilteredTransactions(filterValue: FilterValue): List<Transaction> {
        filterValue.type = filterValue.type ?: listOf(
            TransactionType.INCOME, TransactionType.EXPENSE
        )
        filterValue.category = filterValue.category ?: categoriesRepository.getAllCategories().value
        filterValue.dateFrom = filterValue.dateFrom ?: Date()
        filterValue.dateTill = filterValue.dateTill ?: Date()

        return transactionsMutableStateFlow.value
            .filter { transaction ->
                filterValue.type!!.any { transaction.type == it }
            }
            .filter { transaction ->
                filterValue.category!!.any { transaction.category == it }
            }
            .filter { transaction ->
                transaction.date >= filterValue.dateFrom &&
                        transaction.date <= filterValue.dateTill
            }
    }

}