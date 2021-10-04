package com.alxbyd.cashcare.utils.dataclasses

import java.util.*

data class Transaction(
    val id: Int? = null,
    val amount: Double,
    val type: TransactionType,
    val date: Date,
    val category: Category,
    val comment: String = "",
)