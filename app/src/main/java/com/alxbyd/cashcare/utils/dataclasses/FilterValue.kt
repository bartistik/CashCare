package com.alxbyd.cashcare.utils.dataclasses

import java.util.*

data class FilterValue(
    var type: List<TransactionType>?,
    var dateFrom: Date?,
    var dateTill: Date?,
    var category: List<Category>?,
)