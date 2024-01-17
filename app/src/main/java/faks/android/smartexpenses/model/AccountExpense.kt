package faks.android.smartexpenses.model

import androidx.room.Embedded
import androidx.room.Relation

data class AccountExpense(
    @Embedded val account: Account,
    @Relation(
        parentColumn = "name",
        entityColumn = "account_id"
    )
    val expensesByAccount: List<Expense>
)
