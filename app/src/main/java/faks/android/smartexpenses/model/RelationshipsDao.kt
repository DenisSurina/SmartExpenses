package faks.android.smartexpenses.model

import androidx.room.Dao
import androidx.room.Query

@Dao
interface RelationshipsDao {

    // Account query's
    @Query("SELECT * FROM account")
    fun getAccountExpenses(): List<AccountExpense>

    @Query("SELECT * FROM account")
    fun getAccountIncomes(): List<AccountIncome>


    // Category query's
    @Query("SELECT * FROM category")
    fun getCategoryExpenses(): List<CategoryExpense>

    @Query("SELECT * FROM account")
    fun getCategoryIncomes(): List<CategoryIncome>

}