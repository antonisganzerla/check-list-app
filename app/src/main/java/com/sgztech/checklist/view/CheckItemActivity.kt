package com.sgztech.checklist.view

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.sgztech.checklist.R
import com.sgztech.checklist.core.CoreApplication
import com.sgztech.checklist.model.CheckItem
import com.sgztech.checklist.util.AlertDialogUtil
import com.sgztech.checklist.util.SnackBarUtil
import kotlinx.android.synthetic.main.activity_check_item.*
import kotlinx.android.synthetic.main.check_item.view.*
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
                    loadOnView(
                        CheckItem(
                            name = this.toString(),
                            idCheckList = idCheckList
                        )
                    )
                    showMessage(R.string.message_check_item_added)
                    this.clear()
                }
            }
            .create()
    }

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
        SnackBarUtil.showShort(llItem, resourceString)
    }

    private fun loadOnView(checkItem: CheckItem) {
        val viewCheckItem = layoutInflater.inflate(R.layout.check_item, null)
        with(viewCheckItem.cbCheckItem) {
            this.text = checkItem.name
            this.isChecked = checkItem.isDone
            if(isChecked){
                overideText()
            }
            this.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    overideText()
                } else {
                    normalText()
                }
            }
        }
        viewCheckItem.tvId.text = checkItem.id.toString()
        llItem.addView(viewCheckItem)
    }

    private fun CheckBox.normalText() {
        this.paintFlags = this.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
    }

    private fun CheckBox.overideText() {
        this.paintFlags = this.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        finish()
        return true
    }

    private suspend fun loadCheckItemList(): MutableList<CheckItem> {
        val result = GlobalScope.async {
            val dao = CoreApplication.database?.checkItemDao()
            dao?.loadbyCheckList(idCheckList)
        }
        return result.await()?.toMutableList() ?: mutableListOf()
    }

    private fun loadData() {
        GlobalScope.launch(context = Dispatchers.Main) {
            val checkItemList = loadCheckItemList()
            for (checkItem in checkItemList) {
                loadOnView(checkItem)
            }
        }
    }

    private fun save(checkItem: CheckItem) {
        GlobalScope.launch {
            val dao = CoreApplication.database?.checkItemDao()
            dao?.add(checkItem)
        }
    }

    override fun onDestroy() {
        for (index in 0 until llItem.childCount) {
            val viewCheckItem = llItem.getChildAt(index)
            val id = viewCheckItem.tvId.text.toString().toLong()
            val name = viewCheckItem.cbCheckItem.text.toString()
            val isDone = viewCheckItem.cbCheckItem.isChecked
            if (id == 0L) {
                save(
                    CheckItem(
                        name = name,
                        isDone = isDone,
                        idCheckList = idCheckList
                    )
                )
            } else {
                save(
                    CheckItem(id, name, isDone, idCheckList)
                )
            }
        }
        super.onDestroy()
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
