package com.sgztech.checklist.view

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.sgztech.checklist.R
import com.sgztech.checklist.util.AlertDialogUtil
import com.sgztech.checklist.util.SnackBarUtil
import kotlinx.android.synthetic.main.activity_check_item.*
import kotlinx.android.synthetic.main.check_item.view.*
import kotlinx.android.synthetic.main.dialog_default_add.view.*
import kotlinx.android.synthetic.main.fab.*
import kotlinx.android.synthetic.main.toolbar.*


class CheckItemActivity : AppCompatActivity() {

    private val dialogView: View by lazy {
        layoutInflater.inflate(R.layout.dialog_default_add, null)
    }

    private val dialog: AlertDialog by lazy {
        AlertDialogUtil.buildCustomDialog(
            this,
            R.string.dialog_add_check_item_title,
            dialogView
        )
            .setPositiveButton(R.string.dialog_save) { _, _ ->
                with(dialogView.etName.text){
                    saveCheckItem(this.toString())
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

    private fun saveCheckItem(name: String) {
        val viewCheckItem = layoutInflater.inflate(R.layout.check_item, null)
        with(viewCheckItem.cbCheckItem){
            this.text = name
            this.setOnCheckedChangeListener { _, isChecked ->
                if(isChecked){
                    this.paintFlags = this.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                }else{
                    this.paintFlags = this.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                }
            }
        }
        llItem.addView(viewCheckItem)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    companion object {

        private const val TOOLBAR_TITLE = "TOOLBAR_TITLE"

        fun open(context: Context, name: String) {
            val intent = Intent(context, CheckItemActivity::class.java)
            intent.putExtra(TOOLBAR_TITLE, name)
            context.startActivity(intent)
        }
    }
}
