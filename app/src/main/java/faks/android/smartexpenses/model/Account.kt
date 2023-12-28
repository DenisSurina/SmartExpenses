package faks.android.smartexpenses.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey



@Entity(tableName = "account")
data class Account (
    @PrimaryKey val name: String,
    @ColumnInfo(name = "icon_id") val iconID: Int,
    @ColumnInfo(name = "color_id") val iconColorID: Int,
    @ColumnInfo(name = "balance") val balance: String?
)