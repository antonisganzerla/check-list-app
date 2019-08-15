package com.sgztech.checklist.view


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.sgztech.checklist.R
import com.sgztech.checklist.adapter.CheckListAdapter
import com.sgztech.checklist.core.CoreApplication
import com.sgztech.checklist.extension.gone
import com.sgztech.checklist.extension.visible
import com.sgztech.checklist.model.CheckList
import com.sgztech.checklist.util.AlertDialogUtil
import com.sgztech.checklist.util.SnackBarUtil
import kotlinx.android.synthetic.main.dialog_default_add.view.*
import kotlinx.android.synthetic.main.fab.*
import kotlinx.android.synthetic.main.fragment_check_list.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


class CheckListFragment : Fragment() {

    private val dialog: AlertDialog by lazy {
        AlertDialogUtil.buildCustomDialog(
            this.requireContext(),
            R.string.dialog_add_check_list_title,
            dialogView
        )
            .setPositiveButton(R.string.dialog_save) { _, _ ->
                with(dialogView.etName.text)      {
                    saveCheckList(this.toString())
                    this.clear()
                }
            }
            .create()
    }

    private val dialogView: View by lazy {
        layoutInflater.inflate(R.layout.dialog_default_add, null)
    }

    private var list = mutableListOf<CheckList>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_check_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupDialogView()
        setupFab()
        loadData()
    }

    private fun loadData() {
        GlobalScope.launch(context = Dispatchers.Main) {
            loadCheckList()
            setupRecyclerView()
        }
    }

    private fun setupRecyclerView() {
        recycler_view_check_list.let {
            it.adapter = CheckListAdapter(list)
            it.layoutManager = LinearLayoutManager(activity)
            it.setHasFixedSize(true)
        }
        setupListVisibility(list)
    }

    private fun setupFab() {
        fab.setOnClickListener {
            dialog.show()
        }
    }

    private fun setupDialogView() {
        dialogView.tvName.text = getString(R.string.tvNameChecklist)
    }

    private fun setupListVisibility(list: MutableList<CheckList>) {
        if (list.isEmpty()) {
            recycler_view_check_list.gone()
            tv_empty_check_list.visible()
        } else {
            recycler_view_check_list.visible()
            tv_empty_check_list.gone()
        }
    }

    private suspend fun loadCheckList(){
        val result = GlobalScope.async {
            val dao = CoreApplication.database?.checkListDao()
            dao?.all()
        }
        list = result.await()?.toMutableList() ?: mutableListOf()
    }


    private fun showMessage(resourceMessage: Int) {
        this.view?.let {
            SnackBarUtil.show(it, resourceMessage)
        }
    }

    private fun saveCheckList(name: String) {
        val checkList = CheckList(name = name)
        GlobalScope.launch {
            val dao = CoreApplication.database?.checkListDao()
            dao?.add(checkList)
        }
        list.add(checkList)
        recycler_view_check_list.adapter?.notifyDataSetChanged()
        showMessage(R.string.message_check_list_added)
    }

}
