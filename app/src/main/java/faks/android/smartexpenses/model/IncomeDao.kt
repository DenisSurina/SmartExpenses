package faks.android.smartexpenses.model

import androidx.room.*


@Dao
interface IncomeDao {

    @Query("SELECT * FROM income")
    fun getAll(): List<Income>

    @Query("SELECT * FROM income WHERE account_id LIKE :accountName")
    fun getIncomeByAccount(accountName: String): List<Income>

    @Query("SELECT * FROM income WHERE category_name LIKE :categoryName")
    fun getIncomeByCategory(categoryName: String): List<Income>

    @Insert
    fun insertAll(vararg users: Income)

    @Update
    fun update(income: Income)

    @Delete
    fun delete(user: Income)


}