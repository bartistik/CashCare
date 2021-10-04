package com.alxbyd.cashcare.ui.basemvp

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

abstract class BasePresenter<V : BaseContract.View> : BaseContract.Presenter<V> {

    protected var view: V? = null
    protected var presenterScope: CoroutineScope? = null
    private var job: Job? = null

    override fun attachView(view: V) {
        this.view = view
        this.job = Job()
        this.presenterScope = CoroutineScope(job!! + Dispatchers.Main)
    }

    override fun detachView() {
        this.view = null
        job?.cancel()
        this.presenterScope = null
    }

}