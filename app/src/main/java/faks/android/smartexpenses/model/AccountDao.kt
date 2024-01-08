package faks.android.smartexpenses.model

import androidx.room.*
import java.math.BigDecimal


@Dao
interface AccountDao {

    @Query("SELECT * FROM account")
    fun getAll(): List<Account>

    @Query("SELECT balance FROM account")
    fun getBalances(): List<BigDecimal>

    @Query("SELECT * FROM account WHERE name LIKE :accountName LIMIT 1")
    fun findByName(accountName: String): Account?

    @Insert
    fun insertAll(vararg users: Account)

    @Update
    fun update(account: Account)

    @Delete
    fun delete(user: Account)


}