package com.sgztech.checklist.util

import android.content.Context
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.sgztech.checklist.R

object AlertDialogUtil {

    /**
     * builder with positive and negative callback
     */
    @JvmStatic
    fun create(
        context: Context,
        resourceMessage: Int,
        positiveCallBack : () -> Unit,
        negativeCallBack : () -> Unit
        ): AlertDialog{
        return buildDefault(context, resourceMessage, positiveCallBack)
            .setNegativeButton(context.getString(R.string.dialog_negative_button)) { _, _ ->
                negativeCallBack()
            }
            .create()
    }

    /**
     * builder with only positive callback
     */
    @JvmStatic
    fun create(
        context: Context,
        resourceMessage: Int,
        positiveCallBack : () -> Unit
    ): AlertDialog{
        return buildDefault(context, resourceMessage, positiveCallBack)
            .setNegativeButton(context.getString(R.string.dialog_negative_button)) { _, _ ->
                // unnecessary negative callback
            }
            .create()
    }

    private fun buildDefault(
        context: Context,
        resourceMessage: Int,
        positiveCallBack : () -> Unit
    ): AlertDialog.Builder{
        return buildSimpleDialog(context, R.string.dialog_title, resourceMessage)
            .setPositiveButton(context.getString(R.string.dialog_positive_button)) { _, _ ->
                positiveCallBack()
            }
    }

    @JvmStatic
    fun buildSimpleDialog(context: Context, resourceTitle: Int, resourceMessage: Int): AlertDialog.Builder{
        return buildSimpleDialog(context, resourceTitle)
            .setMessage(context.getString(resourceMessage))
    }

    @JvmStatic
    fun buildSimpleDialog(context: Context, resourceTitle: Int): AlertDialog.Builder{
        return AlertDialog.Builder(context)
            .setTitle(context.getString(resourceTitle))
    }

    fun buildCustomDialog(context: Context, resourceTitle: Int, view: View): AlertDialog.Builder{
       return buildSimpleDialog(context, resourceTitle)
            .setView(view)
    }

    @JvmStatic
    fun showSimpleDialog(context: Context, resourceTitle: Int, resourceMessage: Int){
        buildSimpleDialog(context, resourceTitle, resourceMessage).show()
    }
}