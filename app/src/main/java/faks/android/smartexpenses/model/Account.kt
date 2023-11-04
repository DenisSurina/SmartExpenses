package faks.android.smartexpenses.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey



@Entity(tableName = "account")
data class Account (
    @PrimaryKey val account_id: Int,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "type") val account: String,
    @ColumnInfo(name = "icon_id") val iconID: String,
    @ColumnInfo(name = "balance") val balance: String?
)