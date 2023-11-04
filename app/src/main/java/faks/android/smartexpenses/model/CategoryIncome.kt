package faks.android.smartexpenses.model

import androidx.room.Embedded
import androidx.room.Relation

data class CategoryIncome(
    @Embedded val category: Category,
    @Relation(
        parentColumn = "category_id",
        entityColumn = "category"
    )
    val incomesByCategory: List<Income>
)
