package com.sgztech.checklist.adapter

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.sgztech.checklist.R
import com.sgztech.checklist.model.CheckItem
import com.sgztech.checklist.repository.CheckItemRepository
import com.sgztech.checklist.util.AlertDialogUtil
import com.sgztech.checklist.util.SnackBarUtil
import kotlinx.android.synthetic.main.check_item_card_view.view.*

class CheckItemAdapter(
    private val repository: CheckItemRepository
) : RecyclerView.Adapter<CheckItemAdapter.CheckItemViewHolder>() {

    private var list: List<CheckItem> = ArrayList()
    private var idCheckList: Long = 0L

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

    fun setCheckLists(checkItens: List<CheckItem>) {
        this.list = checkItens
        notifyDataSetChanged()
    }

    fun setIdCheckList(idCheckList: Long){
        this.idCheckList = idCheckList
    }

    inner class CheckItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(checkItem: CheckItem, position: Int) {
            with(itemView.cbCheckItem) {
                this.text = checkItem.name
                this.isChecked = checkItem.isDone
                if (isChecked) {
                    overideText()
                }
                this.setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked) {
                        overideText()
                    } else {
                        normalText()
                    }
                    save(
                        CheckItem(
                            itemView.tvId.text.toString().toLong(),
                            itemView.cbCheckItem.text.toString(),
                            isChecked,
                            idCheckList
                        )
                    )
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
                repository.delete(checkItem)
                notifyItemRemoved(position)
                SnackBarUtil.show(itemView, R.string.message_delete_item)
            }
        }
    }

    fun save(checkItem: CheckItem) {
        repository.insert(checkItem)
    }

    private fun CheckBox.normalText() {
        this.paintFlags = this.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
    }

    private fun CheckBox.overideText() {
        this.paintFlags = this.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
    }
}