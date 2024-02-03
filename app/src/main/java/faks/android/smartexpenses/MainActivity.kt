package faks.android.smartexpenses

import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatDelegate
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import faks.android.smartexpenses.contoller.AccountManagementActivity
import faks.android.smartexpenses.databinding.ActivityMainBinding
import faks.android.smartexpenses.contoller.CategoriesFragment
import faks.android.smartexpenses.contoller.ReportsFragment
import faks.android.smartexpenses.contoller.HomeFragment
import faks.android.smartexpenses.model.Account
import faks.android.smartexpenses.model.Category
import faks.android.smartexpenses.model.SmartExpensesLocalDatabase
import java.math.BigDecimal
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    private lateinit var bottomNavigationView: BottomNavigationView


    companion object{

        const val LIGHT = "Light"
        const val DARK = "Dark"
        const val DEFAULT_PREFERENCES = "default_preferences"
        const val THEME_PREFERENCES = "theme_pref"

    }

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

        GlobalScope.launch {
           prepopulateDatabase()
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


        binding.navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_account -> {
                    val intent = Intent(this, AccountManagementActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.nav_settings -> {
                    // Handle the nav_settings action

                    val settingsText = menuItem.title.toString()
                    val prefs = this.getSharedPreferences(DEFAULT_PREFERENCES, Context.MODE_PRIVATE)

                    if(settingsText == "Dark Theme"){
                        prefs.edit().putString(THEME_PREFERENCES, DARK).apply()
                        initTheme(DARK)

                    }else{
                        prefs.edit().putString(THEME_PREFERENCES, LIGHT).apply()
                        initTheme(LIGHT)
                    }

                    true
                }
                else -> false
            }
        }

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



    private fun prepopulateDatabase(){


        val db: SmartExpensesLocalDatabase = SmartExpensesLocalDatabase.getDatabase(application)

        // DEFAULT INCOME CATEGORY SETUP
        val defaultIncomeCategory = db.categoryDao().getByCategoryName(SmartExpensesLocalDatabase.DEFAULT_CATEGORY_INCOME)
        if(defaultIncomeCategory == null){

            val categoryName = SmartExpensesLocalDatabase.DEFAULT_CATEGORY_INCOME
            val type = CategoriesFragment.INCOME
            val iconId =  R.drawable.sunbed
            val colorID = Color.GRAY

            val newCategoryDefault = Category(categoryName,type,iconId,colorID)
            db.categoryDao().insertAll(newCategoryDefault)

        }

        // DEFAULT EXPENSE CATEGORY SETUP
        val defaultExpenseCategory = db.categoryDao().getByCategoryName(SmartExpensesLocalDatabase.DEFAULT_CATEGORY_EXPENSE)
        if(defaultExpenseCategory == null){

            val categoryName = SmartExpensesLocalDatabase.DEFAULT_CATEGORY_EXPENSE
            val type = CategoriesFragment.EXPENSE
            val iconId =  R.drawable.sunbed
            val colorID = Color.GRAY

            val newCategoryDefault = Category(categoryName,type,iconId,colorID)
            db.categoryDao().insertAll(newCategoryDefault)

        }


        // DEFAULT ACCOUNT SETUP
        val defaultAccount = db.accountDao().findByName(SmartExpensesLocalDatabase.OTHERS)
        if(defaultAccount == null){

            val accountName = SmartExpensesLocalDatabase.OTHERS
            val iconId =  R.drawable.sunbed
            val colorID = Color.GRAY
            val balance = BigDecimal("0")

            val newDefaultAccount = Account(accountName,iconId,colorID,balance)
            db.accountDao().insertAll(newDefaultAccount)

        }

    }

    private fun initTheme(theme : String){

        val menuItem = binding.navView.menu.findItem(R.id.nav_settings)

        if(theme == DARK){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            menuItem.title = "Light Theme"
        }else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            menuItem.title = "Dark Theme"
        }
    }


}