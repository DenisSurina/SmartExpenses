package faks.android.smartexpenses.model

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query


@Dao
interface IncomeDao {

    @Query("SELECT * FROM income")
    fun getAll(): List<Income>

    @Insert
    fun insertAll(vararg users: Income)

    @Delete
    fun delete(user: Income)


}