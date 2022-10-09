package com.sgztech.checklist.adapter

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.sgztech.checklist.R
import com.sgztech.checklist.model.CheckItem
import com.sgztech.checklist.util.AlertDialogUtil

class CheckItemAdapter(
    private val items: List<CheckItem>,
    private val updateCallback: (checkItem: CheckItem) -> Unit,
    private val deleteCallback: (checkItem: CheckItem) -> Unit,
) : RecyclerView.Adapter<CheckItemAdapter.CheckItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CheckItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.check_item_card_view, parent, false)
        return CheckItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: CheckItemViewHolder, position: Int) {
        holder.bind(items[position])
    }

    inner class CheckItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val cbCheckItem: CheckBox by lazy { itemView.findViewById(R.id.cbCheckItem) }
        private val tvId: TextView by lazy { itemView.findViewById(R.id.tvId) }
        private val btnDeleteCheckItem: ImageButton by lazy { itemView.findViewById(R.id.btnDeleteCheckItem) }

        fun bind(checkItem: CheckItem) {
            cbCheckItem.apply {
                this.text = checkItem.name
                this.isChecked = checkItem.isDone
                if (isChecked)
                    overrideText()
                else
                    normalText()

                this.setOnCheckedChangeListener { _, isChecked ->
                    updateCallback(checkItem.copy(isDone = isChecked))
                }
            }
            tvId.text = checkItem.id.toString()
            btnDeleteCheckItem.setOnClickListener {
                createAlertDialog(checkItem).show()
            }
        }

        private fun createAlertDialog(checkItem: CheckItem): AlertDialog {
            return AlertDialogUtil.create(
                itemView.context,
                R.string.dialog_message_delete_check_item
            ) {
                deleteCallback(checkItem)
                notifyItemRemoved(adapterPosition)
            }
        }
    }

    private fun CheckBox.normalText() {
        this.paintFlags = this.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
    }

    private fun CheckBox.overrideText() {
        this.paintFlags = this.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
    }
}