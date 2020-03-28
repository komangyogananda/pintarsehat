package com.kulguy.pintarsehat

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.WindowManager
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.android.synthetic.main.dashboard_content.*

class DashboardActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private val fragmentManager: FragmentManager = supportFragmentManager
    private val fragment1: Fragment = SearchFragment()
    private val fragment2: Fragment = PhotoFragment()
    private val fragment3: Fragment = DailyNutritionFragment()
    private var activeFragment: Fragment = fragment1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setSupportActionBar(toolbar)
        setContentView(R.layout.activity_dashboard)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.firebase_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        auth = FirebaseAuth.getInstance()

        bottom_navigation.menu.getItem(0).isChecked = true
        activeFragment = fragment1
        fragmentManager.beginTransaction().add(dashboard_content.id, fragment3, "fragment3").hide(fragment3).commit()
        fragmentManager.beginTransaction().add(dashboard_content.id, fragment2, "fragment2").hide(fragment2).commit()
        fragmentManager.beginTransaction().add(dashboard_content.id, fragment1, "fragment1").commit()

        bottom_navigation.setOnNavigationItemSelectedListener(mBottomNavigationOnNavigationSelectedListener)
    }

    private val mBottomNavigationOnNavigationSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { menuItem ->
        when (menuItem.itemId){
            R.id.navigation_photo -> {
                fragmentManager.beginTransaction().hide(activeFragment).show(fragment2).commit()
                activeFragment = fragment2
            }
            R.id.navigation_nutrionist -> {
                fragmentManager.beginTransaction().hide(activeFragment).show(fragment1).commit()
                activeFragment = fragment1
            }
            R.id.navigation_daily_nutrition -> {
                fragmentManager.beginTransaction().hide(activeFragment).show(fragment3).commit()
                activeFragment = fragment3
            }
        }
        return@OnNavigationItemSelectedListener true
    }

    private fun signOut() {
        // Firebase sign out
        auth.signOut()

        // Google sign out
        googleSignInClient.signOut().addOnCompleteListener(this) {
            startActivity(MainActivity.getLaunchIntent(this))
        }
    }

    companion object {
        fun getLaunchIntent(from: Context) = Intent(from, DashboardActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }
    }
}
