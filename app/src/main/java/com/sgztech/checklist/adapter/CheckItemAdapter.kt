package com.sgztech.checklist.adapter

import android.graphics.Paint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.sgztech.checklist.R
import com.sgztech.checklist.model.CheckItem
import com.sgztech.checklist.util.AlertDialogUtil
import kotlinx.android.synthetic.main.check_item_card_view.view.*

class CheckItemAdapter(
    private val deleteCallback : (checkItem: CheckItem) -> Unit
) : RecyclerView.Adapter<CheckItemAdapter.CheckItemViewHolder>() {

    private var list: List<CheckItem> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CheckItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.check_item_card_view, parent, false)
        return CheckItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: CheckItemViewHolder, position: Int) {
        holder.bind(list[position], position)
    }

    fun setCheckItens(checkItens: List<CheckItem>) {
        this.list = checkItens
        notifyDataSetChanged()
    }

    fun getCheckItens(): List<CheckItem>{
        return list
    }

    inner class CheckItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(checkItem: CheckItem, position: Int) {
            Log.w("DEBUG", "bind no adaptger")
            with(itemView.cbCheckItem) {
                this.text = checkItem.name
                this.isChecked = checkItem.isDone
                if (isChecked) {
                    overideText()
                }
                this.setOnCheckedChangeListener { _, isChecked ->
                    Log.w("DEBUG", "setOnCheckedChangeListener no adaptger")
                    if (isChecked) {
                        overideText()
                    } else {
                        normalText()
                    }
                    checkItem.isDone = isChecked
                }
            }
            itemView.tvId.text = checkItem.id.toString()
            itemView.btnDeleteCheckItem.setOnClickListener {
                createAlertDialog(checkItem, position).show()
            }
        }

        private fun createAlertDialog(checkItem: CheckItem, position: Int): AlertDialog {
            return AlertDialogUtil.create(
                itemView.context,
                R.string.dialog_message_delete_check_item
            ) {
                deleteCallback(checkItem)
                notifyItemRemoved(position)
            }
        }
    }

    private fun CheckBox.normalText() {
        this.paintFlags = this.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
    }

    private fun CheckBox.overideText() {
        this.paintFlags = this.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
    }
}