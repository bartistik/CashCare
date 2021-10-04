package com.alxbyd.cashcare.ui.basemvp

interface BaseContract {

    interface Presenter<V : View> {
        fun attachView(view: V)
        fun detachView()
        fun handleClickOnExit()
    }

    interface View {
        fun showProgress(shown: Boolean)
        fun showError(error: Throwable)
    }

    interface Router {
        fun exit()
    }

}