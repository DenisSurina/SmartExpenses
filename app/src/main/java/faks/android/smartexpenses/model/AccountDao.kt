package faks.android.smartexpenses.model

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query


@Dao
interface AccountDao {

    @Query("SELECT * FROM account")
    fun getAll(): List<Account>

    @Query("SELECT * FROM account WHERE account_id LIKE :accountID LIMIT 1")
    fun findByName(accountID: String): Account

    @Insert
    fun insertAll(vararg users: Account)

    @Delete
    fun delete(user: Account)


}