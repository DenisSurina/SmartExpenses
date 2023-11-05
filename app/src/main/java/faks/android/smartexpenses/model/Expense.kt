package faks.android.smartexpenses.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "expense")
data class Expense (
    @PrimaryKey val expense_id: Int,
    @ColumnInfo(name = "amount") val amount: String,
    @ColumnInfo(name = "account_id") val account: Int,
    @ColumnInfo(name = "category_id") val category: Int,
    @ColumnInfo(name = "description") val description: String?,
    @ColumnInfo(name = "creation_time") val creationTime: String?,
)