package faks.android.smartexpenses.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.math.BigDecimal
import java.util.Date


@Entity(tableName = "income")
data class Income (
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "income_id") val incomeID: Int = 0,
    @ColumnInfo(name = "amount") val amount: BigDecimal,
    @ColumnInfo(name = "account_id") val account: String,
    @ColumnInfo(name = "category_name") val category_name: String,
    @ColumnInfo(name = "description") val description: String?,
    @ColumnInfo(name = "creation_time") val creationTime: Date,
)