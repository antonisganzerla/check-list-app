package com.sgztech.checklist.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.sgztech.checklist.R
import com.sgztech.checklist.core.CoreApplication
import com.sgztech.checklist.model.CheckList
import com.sgztech.checklist.util.AlertDialogUtil
import com.sgztech.checklist.util.SnackBarUtil
import com.sgztech.checklist.view.CheckItemActivity
import kotlinx.android.synthetic.main.check_list_card_view.view.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class CheckListAdapter (
    private val list: MutableList<CheckList>
    ) : RecyclerView.Adapter<CheckListAdapter.CheckListViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CheckListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.check_list_card_view, parent, false)
        return CheckListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: CheckListViewHolder, position: Int) {
        holder.bind(list[position], position)
    }


    inner class CheckListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(checkList: CheckList, position: Int){
            val name = checkList.name
            val id = checkList.id
            itemView.tvCheckListName.text = name
            itemView.tvDate.text = checkList.createDate
            itemView.btnDeleteCheckList.setOnClickListener {
                createAlertDialog(checkList, position).show()
            }
            itemView.setOnClickListener {
                CheckItemActivity.open(itemView.context, name, id)
            }
        }

        private fun createAlertDialog(checkList: CheckList, position: Int): AlertDialog {
            return AlertDialogUtil.create(
                itemView.context,
                R.string.dialog_message_delete_check_list
            ) {
                GlobalScope.launch {
                    val dao = CoreApplication.database?.checkListDao()
                    dao?.delete(checkList)
                }
                list.remove(checkList)
                notifyItemRemoved(position)
                SnackBarUtil.show(itemView, R.string.message_delete_item)
            }
        }
    }
}