package faks.android.smartexpenses.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import faks.android.smartexpenses.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeFragmentViewModel(application : Application) : AndroidViewModel(application)  {

    private val db: SmartExpensesLocalDatabase = SmartExpensesLocalDatabase.getDatabase(application)



    fun getCategoriesByType(categoryType: String, callback: (List<Category>) -> Unit) {
        viewModelScope.launch {
            val categories = withContext(Dispatchers.IO) {
                db.categoryDao().findByCategoryType(categoryType)
            }
            callback(categories)
        }
    }


    fun getAccounts(callback: (List<Account>)-> Unit){
        viewModelScope.launch{
            val accounts = withContext(Dispatchers.IO){
                db.accountDao().getAll()
            }
            callback(accounts)
        }
    }

    fun insertExpense(expense: Expense){
        viewModelScope.launch(Dispatchers.IO) {
            db.expenseDao().insertAll(expense)
        }
    }

    fun insertIncome(income: Income){
        viewModelScope.launch(Dispatchers.IO) {
            db.incomeDao().insertAll(income)
        }
    }

}