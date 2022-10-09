package com.sgztech.checklist.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputLayout
import com.sgztech.checklist.R
import com.sgztech.checklist.adapter.CheckItemAdapter
import com.sgztech.checklist.extension.gone
import com.sgztech.checklist.extension.showMessage
import com.sgztech.checklist.extension.visible
import com.sgztech.checklist.model.CheckItem
import com.sgztech.checklist.util.AlertDialogUtil
import com.sgztech.checklist.util.CheckNameUtil.isValid
import com.sgztech.checklist.viewModel.CheckItemViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class CheckItemActivity : AppCompatActivity() {

    private val dialogView: View by lazy {
        layoutInflater.inflate(R.layout.dialog_default_add, null)
    }
    private val btnSave: Button by lazy { dialogView.findViewById(R.id.btnSave) }
    private val etName: EditText by lazy { dialogView.findViewById(R.id.etName) }
    private val textInputLayoutName: TextInputLayout by lazy { dialogView.findViewById(R.id.textInputLayoutName) }

    private val toolbar: Toolbar by lazy { findViewById(R.id.toolbar) }
    private val fab: FloatingActionButton by lazy { findViewById(R.id.fab) }
    private val recyclerViewCheckItem: RecyclerView by lazy { findViewById(R.id.recycler_view_check_item) }
    private val panelEmptyList: LinearLayout by lazy { findViewById(R.id.panel_empty_list) }

    private val dialog: AlertDialog by lazy {
        AlertDialogUtil.buildCustomDialog(
            this,
            R.string.dialog_add_check_item_title,
            dialogView
        ).create()
    }

    private var idCheckList: Long = 0
    private val viewModel: CheckItemViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check_item)

        setupToolbar()
        setupDialogView()
        setupFab()
        setupIdCheckList()
        setupAdapter()
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
        btnSave.setOnClickListener {
            if (isValid(etName, textInputLayoutName)) {
                saveCheckItem(etName.text.toString(), idCheckList)
                etName.text.clear()
                dialog.dismiss()
            }
        }
    }

    private fun saveCheckItem(name: String, id: Long) {
        val checkItem = CheckItem(name = name, idCheckList = id)
        viewModel.insert(checkItem)
        fab.showMessage(R.string.message_check_item_added)
        loadData()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        finish()
        return true
    }

    private fun loadData() {
        viewModel.getAllCheckItems(idCheckList)
    }

    private fun setupAdapter() {
        viewModel.checkItems.observe(this) { items ->
            val adapter = CheckItemAdapter(
                items = items,
                updateCallback = { item ->
                    viewModel.update(item)
                    loadData()
                }
            ) { checkItem ->
                viewModel.delete(checkItem)
                fab.showMessage(R.string.message_delete_item)
                loadData()
            }

            recyclerViewCheckItem.apply {
                this.adapter = adapter
                layoutManager = LinearLayoutManager(this@CheckItemActivity)
                setHasFixedSize(true)
            }
            setupListVisibility(items)
        }
    }

    private fun setupListVisibility(items: List<CheckItem>) {
        if (items.isEmpty()) {
            recyclerViewCheckItem.gone()
            panelEmptyList.visible()
        } else {
            recyclerViewCheckItem.visible()
            panelEmptyList.gone()
        }
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
                viewModel.filter(newText, idCheckList)
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
