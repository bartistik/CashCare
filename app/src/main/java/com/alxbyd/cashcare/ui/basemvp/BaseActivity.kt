package com.alxbyd.cashcare.ui.basemvp

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity<V : BaseContract.View, P : BaseContract.Presenter<V>>(
    @LayoutRes val layoutId: Int
) : AppCompatActivity(layoutId), BaseContract.View {

    protected lateinit var presenter: P

    protected abstract fun createPresenter(): P

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = createPresenter()
        presenter.attachView(this as V)
    }

    override fun onDestroy() {
        presenter.detachView()
        super.onDestroy()
    }

    override fun showProgress(shown: Boolean) {
        //none
    }

    override fun showError(error: Throwable) {
        //none
    }

}