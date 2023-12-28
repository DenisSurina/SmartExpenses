package faks.android.smartexpenses.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "income")
data class Income (
    @PrimaryKey @ColumnInfo(name = "income_id") val incomeID: Int,
    @ColumnInfo(name = "amount") val amount: String,
    @ColumnInfo(name = "account_id") val account: String,
    @ColumnInfo(name = "category_name") val category_name: String,
    @ColumnInfo(name = "description") val description: String?,
    @ColumnInfo(name = "creation_time") val creationTime: String?,
)