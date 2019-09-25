package com.sgztech.checklist.view


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.sgztech.checklist.R
import com.sgztech.checklist.adapter.CheckListAdapter
import com.sgztech.checklist.extension.gone
import com.sgztech.checklist.extension.showMessage
import com.sgztech.checklist.extension.visible
import com.sgztech.checklist.model.CheckList
import com.sgztech.checklist.util.AlertDialogUtil
import com.sgztech.checklist.util.CheckNameUtil
import com.sgztech.checklist.viewModel.CheckListViewModel
import kotlinx.android.synthetic.main.dialog_default_add.view.*
import kotlinx.android.synthetic.main.fab.*
import kotlinx.android.synthetic.main.fragment_check_list.*
import org.koin.android.ext.android.inject


class CheckListFragment : Fragment() {

    private val dialog: AlertDialog by lazy {
        AlertDialogUtil.buildCustomDialog(
            this.requireContext(),
            R.string.dialog_add_check_list_title,
            dialogView
        ).create()
    }

    private val dialogView: View by lazy {
        layoutInflater.inflate(R.layout.dialog_default_add, null)
    }

    private val adapter: CheckListAdapter by inject()
    private val viewModel: CheckListViewModel by inject()

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
        setupRecyclerView()
        loadData()
    }

    private fun loadData() {
        viewModel.getAllCheckLists().observe(
            this, Observer {
                adapter.setCheckLists(it)
                setupListVisibility(it)
            }
        )
    }

    private fun setupRecyclerView() {
        recycler_view_check_list.let {
            it.adapter = adapter
            it.layoutManager = LinearLayoutManager(activity)
            it.setHasFixedSize(true)
        }
    }

    private fun setupFab() {
        fab.setOnClickListener {
            dialog.show()
        }
    }

    private fun setupDialogView() {
        dialogView.btnSave.setOnClickListener {
            val etName = dialogView.etName
            if (CheckNameUtil.isValid(etName, dialogView.textInputLayoutName)) {
                saveCheckList(etName.text.toString())
                etName.text.clear()
                dialog.dismiss()
            }
        }
    }

    private fun setupListVisibility(list: List<CheckList>) {
        if (list.isEmpty()) {
            recycler_view_check_list.gone()
            panel_empty_list.visible()
        } else {
            recycler_view_check_list.visible()
            panel_empty_list.gone()
        }
    }

    private fun saveCheckList(name: String) {
        val checkList = CheckList(name = name)
        viewModel.insert(checkList)
        requireActivity().showMessage(fab, R.string.message_check_list_added)
    }

}
