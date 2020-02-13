package com.sgztech.checklist.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.sgztech.checklist.R
import com.sgztech.checklist.adapter.CheckItemAdapter
import com.sgztech.checklist.extension.gone
import com.sgztech.checklist.extension.showMessage
import com.sgztech.checklist.extension.visible
import com.sgztech.checklist.model.CheckItem
import com.sgztech.checklist.util.AlertDialogUtil
import com.sgztech.checklist.util.CheckNameUtil.isValid
import com.sgztech.checklist.viewModel.CheckItemViewModel
import kotlinx.android.synthetic.main.activity_check_item.*
import kotlinx.android.synthetic.main.dialog_default_add.view.*
import kotlinx.android.synthetic.main.fab.*
import kotlinx.android.synthetic.main.toolbar.*
import org.koin.android.ext.android.inject


class CheckItemActivity : AppCompatActivity() {

    private val dialogView: View by lazy {
        layoutInflater.inflate(R.layout.dialog_default_add, null)
    }

    private val dialog: AlertDialog by lazy {
        AlertDialogUtil.buildCustomDialog(
            this,
            R.string.dialog_add_check_item_title,
            dialogView
        ).create()
    }

    private var idCheckList: Long = 0
    private lateinit var adapter: CheckItemAdapter
    private val viewModel: CheckItemViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check_item)

        setupToolbar()
        setupDialogView()
        setupFab()
        setupIdCheckList()
        setupAdapter()
        setupRecyclerView()
        loadData()
    }

    private fun setupIdCheckList() {
        intent.extras?.let {
            idCheckList = it.getLong(CHECK_LIST_ID)
        }
    }

    private fun setupToolbar() {
        intent.extras?.let {
            toolbar.title = it.getString(TOOLBAR_TITLE)
            setSupportActionBar(toolbar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
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
            if (isValid(etName, dialogView.textInputLayoutName)) {
                saveCheckItem(etName.text.toString(), idCheckList)
                etName.text.clear()
                dialog.dismiss()
            }
        }
    }

    private fun saveCheckItem(name: String, id: Long) {
        val checkItem = CheckItem(name = name, idCheckList = id)
        viewModel.insert(checkItem)
        showMessage(recycler_view_check_item, R.string.message_check_item_added)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        finish()
        return true
    }

    private fun loadData() {
        viewModel.getAllCheckItens(idCheckList).observe(
            this,
            Observer {
                adapter.setCheckItens(it.toMutableList())
                setupListVisibility(it)
            }
        )
    }

    private fun setupAdapter() {
        adapter = CheckItemAdapter { checkItem ->
            viewModel.delete(checkItem)
            showMessage(recycler_view_check_item, R.string.message_delete_item)
        }
    }

    private fun setupRecyclerView() {
        recycler_view_check_item.let {
            it.adapter = adapter
            it.layoutManager = LinearLayoutManager(this)
            it.setHasFixedSize(true)
        }
    }

    private fun setupListVisibility(list: List<CheckItem>) {
        if (list.isEmpty()) {
            recycler_view_check_item.gone()
            panel_empty_list.visible()
        } else {
            recycler_view_check_item.visible()
            panel_empty_list.gone()
        }
    }

    override fun onStop() {
        adapter.getCheckItens().forEach {
            viewModel.update(it)
        }
        super.onStop()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.search_menu, menu)

        val searchItem = menu?.findItem(R.id.action_search)
        val searchView = searchItem?.actionView as SearchView
        searchView.imeOptions = EditorInfo.IME_ACTION_DONE

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter.filter(newText)
                return false
            }
        })

        return super.onCreateOptionsMenu(menu)
    }

    companion object {

        private const val TOOLBAR_TITLE = "TOOLBAR_TITLE"
        private const val CHECK_LIST_ID = "CHECK_LIST_ID"

        fun open(context: Context, name: String, idCheckList: Long) {
            val intent = Intent(context, CheckItemActivity::class.java)
            intent.putExtra(TOOLBAR_TITLE, name)
            intent.putExtra(CHECK_LIST_ID, idCheckList)
            context.startActivity(intent)
        }
    }
}
