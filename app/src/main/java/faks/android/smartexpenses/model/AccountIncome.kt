package faks.android.smartexpenses.model

import androidx.room.Embedded
import androidx.room.Relation

data class AccountIncome(
    @Embedded val account: Account,
    @Relation(
        parentColumn = "name",
        entityColumn = "account_id"
    )
    val incomesByAccount: List<Income>
)
