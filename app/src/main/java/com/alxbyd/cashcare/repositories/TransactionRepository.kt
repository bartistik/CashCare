package com.alxbyd.cashcare.repositories

import com.alxbyd.cashcare.utils.dataclasses.FilterValue
import com.alxbyd.cashcare.utils.dataclasses.Transaction

interface TransactionRepository {

    suspend fun addTransaction(transaction: Transaction)
    suspend fun saveTransaction(transaction: Transaction)
    suspend fun deleteTransaction(idTransaction: Int)
    suspend fun getFilteredTransactions(filterValue: FilterValue): List<Transaction>

}