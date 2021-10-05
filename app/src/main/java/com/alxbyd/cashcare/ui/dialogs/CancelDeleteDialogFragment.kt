package com.alxbyd.cashcare.ui.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import com.alxbyd.cashcare.R
import com.alxbyd.cashcare.ui.basemvp.BaseDialogFragment

class CancelDeleteDialogFragment :
    BaseDialogFragment<OnDialogFragmentClickListener>() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(activity)
            .setTitle(arguments?.getString("title"))
            .setMessage(arguments?.getString("msg"))
            .setCancelable(false)
            .setPositiveButton(getString(R.string.ok_button)) { _: DialogInterface, _: Int ->
                activityInstance?.onOkClickedDialog(
                    this
                )
            }
            .setNegativeButton(getString(R.string.cancel_button)) { _: DialogInterface, _: Int ->
                dismiss()
            }
            .create()
    }

    companion object {
        fun newInstance(title: String, message: String): CancelDeleteDialogFragment {
            val dialog = CancelDeleteDialogFragment()
            val args = Bundle()
            args.putString("title", title)
            args.putString("msg", message)
            dialog.arguments = args
            return dialog
        }
    }

}