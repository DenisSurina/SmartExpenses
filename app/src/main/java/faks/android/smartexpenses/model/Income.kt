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
    @ColumnInfo(name = "account_id") val accountID: String,
    @ColumnInfo(name = "category_name") val categoryName: String,
    @ColumnInfo(name = "creation_time") val creationTime: Date,
    @ColumnInfo(name = "description") val description: String?,
)