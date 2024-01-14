package faks.android.smartexpenses.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import faks.android.smartexpenses.model.Account
import faks.android.smartexpenses.model.Expense
import faks.android.smartexpenses.model.Income
import faks.android.smartexpenses.model.SmartExpensesLocalDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.math.BigDecimal

class AccountManagementActivityViewModel(application: Application) : AndroidViewModel(application) {

    private val db: SmartExpensesLocalDatabase = SmartExpensesLocalDatabase.getDatabase(application)


    fun insertAccount(account: Account){
        viewModelScope.launch(Dispatchers.IO){
            db.accountDao().insertAll(account)
        }
    }

    fun deleteAccount(account: Account){
        viewModelScope.launch(Dispatchers.IO){
            db.accountDao().delete(account)
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

    fun getAccountByName(name: String, callback: (Account?)-> Unit){
        viewModelScope.launch{
            val account = withContext(Dispatchers.IO){
                db.accountDao().findByName(name)
            }
            callback(account)
        }
    }

    fun getTransactionsByAccount(accountName: String, callback: ( List<Income>, List<Expense>) -> Unit){
        viewModelScope.launch{
            val incomes = withContext(Dispatchers.IO){
                db.incomeDao().getIncomeByAccount(accountName)
            }
            val expenses = withContext(Dispatchers.IO){
                db.expenseDao().getExpensesByAccount(accountName)
            }
            callback(incomes,expenses)
        }
    }

    fun updateIncome(income: Income){
        viewModelScope.launch(Dispatchers.IO){
            db.incomeDao().update(income)
        }
    }

    fun updateExpense(expense: Expense){
        viewModelScope.launch(Dispatchers.IO){
            db.expenseDao().update(expense)
        }
    }

    fun getBalances(callback: (List<BigDecimal>)-> Unit){
        viewModelScope.launch{
            val balances = withContext(Dispatchers.IO){
                db.accountDao().getBalances()
            }
            callback(balances)
        }
    }

}