package com.alxbyd.cashcare.ui.basemvp

import android.content.Context
import androidx.fragment.app.DialogFragment

abstract class BaseDialogFragment<D> : DialogFragment() {

    protected var activityInstance: D? = null

    override fun onAttach(context: Context) {
        activityInstance = requireParentFragment() as D
        super.onAttach(context)
    }

    override fun onDetach() {
        super.onDetach()
        activityInstance = null
    }

}