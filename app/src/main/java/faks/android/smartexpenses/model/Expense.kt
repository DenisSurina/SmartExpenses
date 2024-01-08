package faks.android.smartexpenses.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.math.BigDecimal
import java.util.*


@Entity(tableName = "expense")
data class Expense (
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "expense_id")val expenseID: Int = 0,
    @ColumnInfo(name = "amount") val amount: BigDecimal,
    @ColumnInfo(name = "account_id") val accountID: String,
    @ColumnInfo(name = "category_name") val categoryName: String,
    @ColumnInfo(name = "creation_time") val creationTime: Date,
    @ColumnInfo(name = "description") val description: String?,
)