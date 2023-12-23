package faks.android.smartexpenses.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "category")
data class Category (
    @PrimaryKey val name: String,
    @ColumnInfo(name = "type") val type: String,
    @ColumnInfo(name = "icon_id") val iconID: Int,
    @ColumnInfo(name = "color_id") val colorID: Int
)