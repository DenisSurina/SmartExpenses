package faks.android.smartexpenses.model

import androidx.room.*


@Dao
interface ExpenseDao {

    @Query("SELECT * FROM expense")
    fun getAll(): List<Expense>

    @Query("SELECT * FROM expense WHERE account_id LIKE :accountName")
    fun getExpensesByAccount(accountName: String): List<Expense>

    @Query("SELECT * FROM expense WHERE category_name LIKE :categoryName")
    fun getExpensesByCategory(categoryName: String): List<Expense>

    @Insert
    fun insertAll(vararg users: Expense)

    @Update
    fun update(expense: Expense)

    @Delete
    fun delete(user: Expense)


}