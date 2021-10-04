package com.alxbyd.cashcare.utils

import android.view.View
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.*

object Ext {

    fun Date.toFormatDate(): String {
        val originalDate = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        return originalDate.format(this)
    }

    fun View.showSnackBar(message: String, duration: Int) {
        Snackbar.make(this, message, duration).show()
    }

    fun Double.toFormatBalanceAmount(): String {
        return "%.${2}f ₴".format(this)
    }

    fun Double.toFormatAmountTransactions(sign: Char): String {
        return "$sign %.${2}f ₴".format(this)
    }

}