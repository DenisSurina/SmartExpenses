package faks.android.smartexpenses.model

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = [Account::class,Category::class,Expense::class,Income::class], version = 3)
abstract class SmartExpensesLocalDatabase : RoomDatabase(){
    abstract fun accountDao(): AccountDao
    abstract fun categoryDao(): CategoryDao
    abstract fun expenseDao(): ExpenseDao
    abstract fun incomeDao(): IncomeDao
    abstract fun relationshipsDao(): RelationshipsDao
}