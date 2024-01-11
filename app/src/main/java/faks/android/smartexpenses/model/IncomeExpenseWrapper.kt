package faks.android.smartexpenses.model

import java.math.BigDecimal
import java.util.Date

sealed class IncomeExpenseWrapper{

    abstract val date: Date
    abstract val amount: BigDecimal
    abstract val type : String

    companion object{
        const val INCOME = "Income"
        const val EXPENSE = "Expense"
    }

    data class IncomeEntry(val income: Income) : IncomeExpenseWrapper() {
        override val date = income.creationTime
        override val amount: BigDecimal = income.amount
        override val type = INCOME
    }

    data class ExpenseEntry(val expense: Expense) : IncomeExpenseWrapper() {
        override val date = expense.creationTime
        override val amount: BigDecimal = expense.amount
        override val type = EXPENSE
    }

}