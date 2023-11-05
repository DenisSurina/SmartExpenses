package faks.android.smartexpenses.model

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface RelationshipsDao {

    // Account query's
    @Transaction
    @Query("SELECT * FROM account")
    fun getAccountExpenses(): List<AccountExpense>

    @Transaction
    @Query("SELECT * FROM account")
    fun getAccountIncomes(): List<AccountIncome>


    // Category query's
    @Transaction
    @Query("SELECT * FROM category")
    fun getCategoryExpenses(): List<CategoryExpense>

    @Transaction
    @Query("SELECT * FROM category")
    fun getCategoryIncomes(): List<CategoryIncome>

}