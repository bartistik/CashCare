package com.alxbyd.cashcare.ui.basemvp

import androidx.appcompat.app.AppCompatActivity

abstract class BaseRouter(
    private val activity: AppCompatActivity
) : BaseContract.Router {

    override fun exit() {
        activity.finish()
    }

}