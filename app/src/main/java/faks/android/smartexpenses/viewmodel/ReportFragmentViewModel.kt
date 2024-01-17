package faks.android.smartexpenses.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import faks.android.smartexpenses.model.AccountExpense
import faks.android.smartexpenses.model.Category
import faks.android.smartexpenses.model.CategoryExpense
import faks.android.smartexpenses.model.SmartExpensesLocalDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ReportFragmentViewModel(application: Application) : AndroidViewModel(application)  {

    private val db: SmartExpensesLocalDatabase = SmartExpensesLocalDatabase.getDatabase(application)


    fun getExpensesByCategory(callback: (List<CategoryExpense>?) -> Unit) {
        viewModelScope.launch {
            val categoryExpenses = withContext(Dispatchers.IO) {
                db.relationshipsDao().getCategoryExpenses()
            }
            callback(categoryExpenses)
        }
    }

    fun getExpensesByAccount(callback: (List<AccountExpense>?) -> Unit) {
        viewModelScope.launch {
            val accountExpenses = withContext(Dispatchers.IO) {
                db.relationshipsDao().getAccountExpenses()
            }
            callback(accountExpenses)
        }
    }

}