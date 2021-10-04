package com.alxbyd.cashcare.ui.basemvp

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import com.alxbyd.cashcare.R

abstract class BaseFragment<V, P>(@LayoutRes layoutId: Int) :
    Fragment(layoutId),
    BaseContract.View
        where
            V : BaseContract.View,
            P : BaseContract.Presenter<V> {

    protected lateinit var toolbar: Toolbar
    protected lateinit var presenter: P

    protected abstract fun createPresenter(): P

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter = createPresenter()
        presenter.attachView(this as V)

        toolbar = ViewCompat.requireViewById(requireView(), R.id.toolbar)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
    }

    override fun onDestroy() {
        presenter.detachView()
        super.onDestroy()
    }

}