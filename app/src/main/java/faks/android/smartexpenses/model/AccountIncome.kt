package faks.android.smartexpenses.model

import androidx.room.Embedded
import androidx.room.Relation

data class AccountIncome(
    @Embedded val account: Account,
    @Relation(
        parentColumn = "account_id",
        entityColumn = "account"
    )
    val incomesByCategory: List<Income>
)
