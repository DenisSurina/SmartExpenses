package faks.android.smartexpenses.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "expense")
data class Expense (
    @PrimaryKey @ColumnInfo(name = "expense_id")val expenseID: Int,
    @ColumnInfo(name = "amount") val amount: String,
    @ColumnInfo(name = "account_id") val accountID: String,
    @ColumnInfo(name = "category_name") val categoryName: String,
    @ColumnInfo(name = "description") val description: String?,
    @ColumnInfo(name = "creation_time") val creationTime: String?,
)