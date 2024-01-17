package faks.android.smartexpenses.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import faks.android.smartexpenses.model.*
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

    fun getIncomeByCategory(callback: (List<CategoryIncome>?) -> Unit) {
        viewModelScope.launch {
            val categoryExpenses = withContext(Dispatchers.IO) {
                db.relationshipsDao().getCategoryIncomes()
            }
            callback(categoryExpenses)
        }
    }

    fun getIncomeByAccount(callback: (List<AccountIncome>?) -> Unit) {
        viewModelScope.launch {
            val accountExpenses = withContext(Dispatchers.IO) {
                db.relationshipsDao().getAccountIncomes()
            }
            callback(accountExpenses)
        }
    }

}