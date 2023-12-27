package faks.android.smartexpenses

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import faks.android.smartexpenses.databinding.ActivityMainBinding
import faks.android.smartexpenses.contoller.CategoriesFragment
import faks.android.smartexpenses.contoller.ReportsFragment
import faks.android.smartexpenses.contoller.HomeFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        createActionBar()
        setupBottomNavigation()

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, HomeFragment() )
                .commit()
        }


    }


    private fun createActionBar(){
        // drawer layout instance to toggle the menu icon to open
        // drawer and back button to close drawer
        drawerLayout = binding.myDrawerLayout
        actionBarDrawerToggle = ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close)

        // pass the Open and Close toggle for the drawer layout listener
        // to toggle the button
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

        // to make the Navigation drawer icon always appear on the action bar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Transactions"
    }


    private fun setupBottomNavigation() {
        bottomNavigationView = binding.bottomNavigationView
        bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navigation_home -> {
                    supportActionBar?.title = "Transactions"
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, HomeFragment())
                        .commit()
                    true
                }
                R.id.navigation_dashboard -> {
                    supportActionBar?.title = "Report Graphs"
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, ReportsFragment())
                        .commit()
                    true
                }
                R.id.navigation_profile -> {
                    supportActionBar?.title = "Categories"
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, CategoriesFragment())
                        .commit()
                    true
                }
                else -> false
            }
        }
    }



    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            true
        } else super.onOptionsItemSelected(item)
    }
}