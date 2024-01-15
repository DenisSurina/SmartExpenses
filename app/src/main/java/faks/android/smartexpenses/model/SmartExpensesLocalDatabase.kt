package faks.android.smartexpenses.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters


@Database(entities = [Account::class,Category::class,Expense::class,Income::class], version = 9)
@TypeConverters(Converters::class)
abstract class SmartExpensesLocalDatabase : RoomDatabase(){
    abstract fun accountDao(): AccountDao
    abstract fun categoryDao(): CategoryDao
    abstract fun expenseDao(): ExpenseDao
    abstract fun incomeDao(): IncomeDao
    abstract fun relationshipsDao(): RelationshipsDao


    companion object {
        @Volatile
        private var INSTANCE: SmartExpensesLocalDatabase? = null
        const val OTHERS = "others"
        const val DEFAULT_CATEGORY_INCOME = "default_category_income"
        const val DEFAULT_CATEGORY_EXPENSE = "default_category_expense"


        fun getDatabase(context: Context): SmartExpensesLocalDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SmartExpensesLocalDatabase::class.java,
                    "main_db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}