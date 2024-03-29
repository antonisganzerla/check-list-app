package com.sgztech.checklist.view


import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.material.navigation.NavigationView
import com.sgztech.checklist.R
import com.sgztech.checklist.extension.openActivity
import com.sgztech.checklist.util.AlertDialogUtil
import com.sgztech.checklist.util.GoogleSignInUtil.signOut
import com.sgztech.checklist.util.SnackBarUtil.show
import com.squareup.picasso.Picasso

class MainActivity : AppCompatActivity() {

    private val account: GoogleSignInAccount? by lazy {
        GoogleSignIn.getLastSignedInAccount(this)
    }
    private var fragmentPosition = -1

    private val toolbar: Toolbar by lazy { findViewById(R.id.toolbar) }
    private val drawerLayout: DrawerLayout by lazy { findViewById(R.id.drawerLayout) }
    private val navView: NavigationView by lazy { findViewById(R.id.navView) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        savedInstanceState?.let {
            fragmentPosition = it.getInt(CURRENT_FRAGMENT_KEY)
        }

        setupToolbar()
        setupDrawer()
        openCheckListFragment()
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        outState.putInt(CURRENT_FRAGMENT_KEY, fragmentPosition)
        super.onSaveInstanceState(outState, outPersistentState)
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
    }

    private fun setupDrawer() {
        val toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_drawer, R.string.close_drawer)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        setupDrawerItemClickListener()
        setupHeaderDrawer()
    }

    private fun setupDrawerItemClickListener() {
        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_item_check_list -> {
                    openCheckListFragment()
                }
                R.id.nav_item_tools -> {
                    displayView(POSITION_PREFERENCES_FRAGMENT, getString(R.string.title_preferences_fragment))
                }
                R.id.nav_item_logout -> {
                    showDialogLogout()
                }
                R.id.nav_item_rate -> {
                    rateApp()
                }
                R.id.nav_item_share -> {
                    shareApp()
                }
                R.id.nav_item_about -> {
                    AlertDialogUtil.showSimpleDialog(
                        this,
                        R.string.dialog_about_app_title,
                        R.string.dialog_about_app_message
                    )
                }
            }
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }
    }

    private fun showDialogLogout() {
        val dialog = AlertDialogUtil.create(
            this,
            R.string.dialog_message_logout
        ) {
            logout()
        }
        dialog.show()
    }

    private fun logout() {
        signOut(this)
        openActivity(LoginActivity::class.java)
    }

    private fun setupHeaderDrawer() {
        val headerView = navView.getHeaderView(0)
        headerView?.let {
            it.findViewById<TextView>(R.id.nav_header_name).text = account?.displayName
            it.findViewById<TextView>(R.id.nav_header_email).text = account?.email
            Picasso.get().load(account?.photoUrl).into(it.findViewById<ImageView>(R.id.nav_header_imageView))
        }
    }

    private fun openCheckListFragment() {
        displayView(POSITION_CHECK_LISTS_FRAGMENT, getString(R.string.title_check_list_fragment))
    }

    private fun displayView(position: Int, title: String) {
        if (position != fragmentPosition) {
            val fragment = when (position) {
                POSITION_CHECK_LISTS_FRAGMENT -> CheckListFragment()
                POSITION_PREFERENCES_FRAGMENT -> PreferencesFragment()
                else -> {
                    CheckListFragment()
                }
            }
            supportFragmentManager.beginTransaction().replace(R.id.content_frame, fragment, null)
                .commitAllowingStateLoss()
            fragmentPosition = position
        }
        toolbar.title = title
    }

    private fun rateApp() {
        val uri = Uri.parse(getString(R.string.app_store_url))
        val intent = Intent(Intent.ACTION_VIEW, uri)
        try {
            startActivity(intent)
        } catch (exception: ActivityNotFoundException) {
            show(toolbar, R.string.msg_store_app_not_found)
        }
    }

    private fun shareApp() {
        val intent = Intent(Intent.ACTION_SEND)
        val msg = getString(R.string.app_store_details).plus(getString(R.string.app_store_url))
        intent.putExtra(Intent.EXTRA_TEXT, msg)
        intent.type = "text/plain"

        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        }
    }

    companion object {
        const val POSITION_CHECK_LISTS_FRAGMENT = 0
        const val POSITION_PREFERENCES_FRAGMENT = 1
        const val CURRENT_FRAGMENT_KEY = "CURRENT_FRAGMENT_KEY"
    }
}
