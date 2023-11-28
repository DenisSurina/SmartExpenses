package faks.android.smartexpenses.model

import androidx.room.Embedded
import androidx.room.Relation

data class CategoryExpense(
    @Embedded val category: Category,
    @Relation(
        parentColumn = "name",
        entityColumn = "category_name"
    )
    val incomesByCategory: List<Expense>
)
