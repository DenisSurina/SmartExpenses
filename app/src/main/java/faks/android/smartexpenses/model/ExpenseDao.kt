package faks.android.smartexpenses.model

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query


@Dao
interface ExpenseDao {

    @Query("SELECT * FROM expense")
    fun getAll(): List<Expense>

    @Query("SELECT * FROM expense WHERE expense_id LIKE :expenseID LIMIT 1")
    fun findByName(expenseID: String): Expense

    @Insert
    fun insertAll(vararg users: Expense)

    @Delete
    fun delete(user: Expense)


}