package com.sgztech.checklist.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.sgztech.checklist.R
import com.sgztech.checklist.model.CheckList
import com.sgztech.checklist.util.AlertDialogUtil
import com.sgztech.checklist.view.CheckItemActivity

class CheckListAdapter (
    private val items: List<CheckList>,
    private val deleteCallback : (checklist: CheckList) -> Unit
) : RecyclerView.Adapter<CheckListAdapter.CheckListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CheckListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.check_list_card_view, parent, false)
        return CheckListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: CheckListViewHolder, position: Int) {
        holder.bind(items[position])
    }

    inner class CheckListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvCheckListName: TextView by lazy { itemView.findViewById(R.id.tvCheckListName) }
        private val tvDate: TextView by lazy { itemView.findViewById(R.id.tvDate) }
        private val btnDeleteCheckList: ImageButton by lazy { itemView.findViewById(R.id.btnDeleteCheckList) }

        fun bind(checkList: CheckList){
            val name = checkList.name
            val id = checkList.id
            tvCheckListName.text = name
            tvDate.text = checkList.createDate
            btnDeleteCheckList.setOnClickListener {
                createAlertDialog(checkList).show()
            }
            itemView.setOnClickListener {
                CheckItemActivity.open(itemView.context, name, id)
            }
        }

        private fun createAlertDialog(checkList: CheckList): AlertDialog {
            return AlertDialogUtil.create(
                itemView.context,
                R.string.dialog_message_delete_check_list
            ) {
                deleteCallback(checkList)
                notifyItemRemoved(adapterPosition)
            }
        }
    }
}