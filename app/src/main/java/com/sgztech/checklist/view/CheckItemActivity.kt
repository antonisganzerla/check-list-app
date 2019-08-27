package com.sgztech.checklist.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.sgztech.checklist.R
import com.sgztech.checklist.adapter.CheckItemAdapter
import com.sgztech.checklist.core.CoreApplication
import com.sgztech.checklist.model.CheckItem
import com.sgztech.checklist.util.AlertDialogUtil
import com.sgztech.checklist.util.SnackBarUtil
import kotlinx.android.synthetic.main.activity_check_item.*
import kotlinx.android.synthetic.main.dialog_default_add.view.*
import kotlinx.android.synthetic.main.fab.*
import kotlinx.android.synthetic.main.toolbar.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


class CheckItemActivity : AppCompatActivity() {

    private val dialogView: View by lazy {
        layoutInflater.inflate(R.layout.dialog_default_add, null)
    }

    private var idCheckList: Long = 0

    private val dialog: AlertDialog by lazy {
        AlertDialogUtil.buildCustomDialog(
            this,
            R.string.dialog_add_check_item_title,
            dialogView
        )
            .setPositiveButton(R.string.dialog_save) { _, _ ->
                with(dialogView.etName.text) {
                    saveCheckItem(this.toString(), idCheckList)
                    this.clear()
                }
            }
            .create()
    }

    private fun saveCheckItem(name: String, id: Long) {
        val checkItem = CheckItem(name = name, idCheckList = id)
        val adapter: CheckItemAdapter = recycler_view_check_item.adapter as CheckItemAdapter
        adapter.save(checkItem)
        adapter.notifyDataSetChanged()
        list.add(checkItem)
        showMessage(R.string.message_check_item_added)
    }

    private var list = mutableListOf<CheckItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check_item)

        setupToolbar()
        setupDialogView()
        setupFab()
        setupIdCheckList()
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
        dialogView.tvName.text = getString(R.string.tvNameCheckItem)
    }

    private fun showMessage(resourceString: Int) {
        SnackBarUtil.showShort(recycler_view_check_item, resourceString)
    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        finish()
        return true
    }

    private suspend fun loadCheckItemList() {
        val result = GlobalScope.async {
            val dao = CoreApplication.database?.checkItemDao()
            dao?.loadbyCheckList(idCheckList)
        }
        list = result.await()?.toMutableList() ?: mutableListOf()
    }

    private fun loadData() {
        GlobalScope.launch(context = Dispatchers.Main) {
            loadCheckItemList()
            setupRecyclerView()
        }
    }

    private fun setupRecyclerView() {
        recycler_view_check_item.let {
            it.adapter = CheckItemAdapter(list, idCheckList)
            it.layoutManager = LinearLayoutManager(this)
            it.setHasFixedSize(true)
        }
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
