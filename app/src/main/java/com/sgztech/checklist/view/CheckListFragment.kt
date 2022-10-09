package com.sgztech.checklist.view


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputLayout
import com.sgztech.checklist.R
import com.sgztech.checklist.adapter.CheckListAdapter
import com.sgztech.checklist.extension.gone
import com.sgztech.checklist.extension.showMessage
import com.sgztech.checklist.extension.visible
import com.sgztech.checklist.model.CheckList
import com.sgztech.checklist.util.AlertDialogUtil
import com.sgztech.checklist.util.CheckNameUtil
import com.sgztech.checklist.viewModel.CheckListViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

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

    private val btnSave: Button by lazy { dialogView.findViewById(R.id.btnSave) }
    private val etName: EditText by lazy { dialogView.findViewById(R.id.etName) }
    private val textInputLayoutName: TextInputLayout by lazy { dialogView.findViewById(R.id.textInputLayoutName) }

    private val fab: FloatingActionButton by lazy { requireView().findViewById(R.id.fab) }
    private val recyclerViewCheckList: RecyclerView by lazy { requireView().findViewById(R.id.recycler_view_check_list) }
    private val panelEmptyList: LinearLayout by lazy { requireView().findViewById(R.id.panel_empty_list) }

    private val viewModel: CheckListViewModel by viewModel()

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
        setupAdapter()
        loadData()
    }

    private fun loadData() {
        viewModel.loadCheckLists()
    }

    private fun setupAdapter() {
        viewModel.checkLists.observe(requireActivity()){ items ->
            val adapter = CheckListAdapter(
                items = items,
            ) { checklist ->
                viewModel.delete(checklist)
                fab.showMessage( R.string.message_delete_item)
                loadData()
            }

            recyclerViewCheckList.let {
                it.adapter = adapter
                it.layoutManager = LinearLayoutManager(activity)
                it.setHasFixedSize(true)
            }

            setupListVisibility(items)
        }
    }

    private fun setupFab() {
        fab.setOnClickListener {
            dialog.show()
        }
    }

    private fun setupDialogView() {
        btnSave.setOnClickListener {
            if (CheckNameUtil.isValid(etName, textInputLayoutName)) {
                saveCheckList(etName.text.toString())
                etName.text.clear()
                dialog.dismiss()
            }
        }
    }

    private fun setupListVisibility(items: List<CheckList>) {
        if (items.isEmpty()) {
            recyclerViewCheckList.gone()
            panelEmptyList.visible()
        } else {
            recyclerViewCheckList.visible()
            panelEmptyList.gone()
        }
    }

    private fun saveCheckList(name: String) {
        val checkList = CheckList(name = name)
        viewModel.insert(checkList)
        fab.showMessage(R.string.message_check_list_added)
        loadData()
    }
}
