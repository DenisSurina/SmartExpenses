package faks.android.smartexpenses.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import faks.android.smartexpenses.contoller.HomeFragment
import faks.android.smartexpenses.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

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

    fun getAccountByName(name: String, callback: (Account) -> Unit) {
        viewModelScope.launch {
            val account = withContext(Dispatchers.IO) {
                db.accountDao().findByName(name)
            }
            if (account != null) {
                callback(account)
            }
        }
    }

    fun updateAccount(account: Account){
        viewModelScope.launch(Dispatchers.IO) {
            db.accountDao().update(account)
        }
    }

    fun insertExpense(expense: Expense){
        viewModelScope.launch(Dispatchers.IO) {
            db.expenseDao().insertAll(expense)
        }
    }

    fun getExpenses(callback: (List<Expense>) -> Unit){
        viewModelScope.launch{
            val expenses = withContext(Dispatchers.IO){
                db.expenseDao().getAll()
            }
            callback(expenses)
        }
    }

    fun insertIncome(income: Income){
        viewModelScope.launch(Dispatchers.IO) {
            db.incomeDao().insertAll(income)
        }
    }

    fun getIncomes(callback: (List<Income>) -> Unit){
        viewModelScope.launch{
            val income = withContext(Dispatchers.IO){
                db.incomeDao().getAll()
            }
            callback(income)
        }
    }

    fun getIncomeExpenseWrapperMapByDateString(dateString : String ,callback: (Map<String, List<IncomeExpenseWrapper>>) -> Unit){
        viewModelScope.launch{
            val wrappers = withContext(Dispatchers.IO){
                val incomes = db.incomeDao().getAll().map{ IncomeExpenseWrapper.IncomeEntry(it) }
                val expenses = db.expenseDao().getAll().map{ IncomeExpenseWrapper.ExpenseEntry(it) }
                val allEntries = incomes + expenses
                val dateFormat = SimpleDateFormat(dateString, Locale.getDefault())
                allEntries.groupBy {entry ->
                    dateFormat.format(entry.date)
                }
            }
            callback(wrappers)
        }
    }



}