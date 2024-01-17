package faks.android.smartexpenses.contoller


import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import faks.android.smartexpenses.R
import faks.android.smartexpenses.databinding.FragmentReportsBinding
import faks.android.smartexpenses.model.SmartExpensesLocalDatabase
import faks.android.smartexpenses.viewmodel.ReportFragmentViewModel
import java.math.BigDecimal

class ReportsFragment : Fragment() {


    private var _binding: FragmentReportsBinding? = null
    private val binding get() = _binding!!

    private var expenseSelected = false

    private lateinit var reportFragmentViewModel: ReportFragmentViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentReportsBinding.inflate(inflater, container, false)
        reportFragmentViewModel = ViewModelProvider(this)[ReportFragmentViewModel::class.java]

        setupButtons()
        setupPieChartsByTransactionType(CategoriesFragment.EXPENSE)

        return binding.root
    }


    private fun setupButtons(){
        val showExpenseGraphs: Button = binding.showExpenseGraphs
        val showIncomeGraphs: Button = binding.showIncomeGraphs

        showExpenseGraphs.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.cyan))
        showIncomeGraphs.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.blue))

        showExpenseGraphs.setOnClickListener {

            setupPieChartsByTransactionType(CategoriesFragment.EXPENSE)
            changeButtonColor(showExpenseGraphs, true)
            changeButtonColor(showIncomeGraphs, false)
        }

        showIncomeGraphs.setOnClickListener {

            setupPieChartsByTransactionType(CategoriesFragment.INCOME)
            changeButtonColor(showExpenseGraphs, false)
            changeButtonColor(showIncomeGraphs, true)
        }
    }

    private fun changeButtonColor(button: Button, isSelected: Boolean) {
        if (isSelected) {
            // Change to selected color
            button.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.cyan))
        } else {
            // Change to default color
            button.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.blue))
        }
    }



    // This function sets up all pie charts in this fragment ( by income or expense)
    private fun setupPieChartsByTransactionType(transactionType : String){


        if (transactionType == CategoriesFragment.INCOME){

            // BY CATEGORY INCOME
            reportFragmentViewModel.getIncomeByCategory {categoryIncomes ->

                if (categoryIncomes != null) {

                    val transactionMap : MutableMap<String, BigDecimal> = sortedMapOf()
                    var totalIncomesByCategories = BigDecimal.ZERO
                    val colors = ArrayList<Int>()
                    val pieChart = binding.fragmentReportsPieChartByCategory.byCategoryPieChartTemplate
                    val linearLayout = binding.fragmentReportsPieChartByCategory.byCategoryPieChartLinearLayout

                    clearTransactionBriefViews(linearLayout,pieChart)

                    for( cat in categoryIncomes){

                        val categoryName = cat.category.name
                        val categoryIncomeSum  = cat.incomesByCategory.sumOf { it.amount }
                        if(categoryName == SmartExpensesLocalDatabase.DEFAULT_CATEGORY_INCOME || categoryIncomeSum.toDouble() <= 0)
                            continue

                        colors.add(cat.category.colorID)
                        transactionMap[categoryName] = categoryIncomeSum

                        totalIncomesByCategories += categoryIncomeSum


                        addTransactionBriefView(linearLayout,categoryName,categoryIncomeSum,cat.category.iconID,cat.category.colorID)

                    }


                    setupPieChart(pieChart,totalIncomesByCategories.toString(), "By Category")
                    loadPieChartData(pieChart, "categories", transactionMap,colors)

                }
            }



            // BY ACCOUNT INCOME
            reportFragmentViewModel.getIncomeByAccount { accountIncomes ->

                if (accountIncomes != null) {

                    val transactionMap : MutableMap<String, BigDecimal> = sortedMapOf()
                    var totalIncomesByAccounts = BigDecimal.ZERO
                    val colors = ArrayList<Int>()
                    val pieChart = binding.fragmentReportsPieChartByAccount.byAccountPieChartTemplate
                    val linearLayout = binding.fragmentReportsPieChartByAccount.byAccountPieChartLinearLayout

                    clearTransactionBriefViews(linearLayout,pieChart)

                    for( acc in accountIncomes){

                        val accountName = acc.account.name
                        val accountIncomeSum  = acc.incomesByAccount.sumOf { it.amount }
                        if(accountName == SmartExpensesLocalDatabase.OTHERS || accountIncomeSum.toDouble() <= 0)
                            continue

                        colors.add(acc.account.iconColorID)
                        transactionMap[accountName] = accountIncomeSum

                        totalIncomesByAccounts += accountIncomeSum


                        addTransactionBriefView(linearLayout,accountName,accountIncomeSum,acc.account.iconID,acc.account.iconColorID)

                    }

                    setupPieChart(pieChart,totalIncomesByAccounts.toString(),"By Account")
                    loadPieChartData(pieChart, "Accounts", transactionMap,colors)

                }

            }



        }else if(transactionType == CategoriesFragment.EXPENSE){


            // BY CATEGORY EXPENSES
            reportFragmentViewModel.getExpensesByCategory {categoryExpenses ->

                if (categoryExpenses != null) {

                    val transactionMap : MutableMap<String, BigDecimal> = sortedMapOf()
                    var totalExpenseByCategories = BigDecimal.ZERO
                    val colors = ArrayList<Int>()
                    val pieChart = binding.fragmentReportsPieChartByCategory.byCategoryPieChartTemplate
                    val linearLayout = binding.fragmentReportsPieChartByCategory.byCategoryPieChartLinearLayout

                    clearTransactionBriefViews(linearLayout,pieChart)

                    for( cat in categoryExpenses){

                        val categoryName = cat.category.name
                        val categoryExpenseSum  = cat.expenseByCategory.sumOf { it.amount }
                        if(categoryName == SmartExpensesLocalDatabase.DEFAULT_CATEGORY_EXPENSE || categoryExpenseSum.toDouble() <= 0)
                            continue

                        colors.add(cat.category.colorID)
                        transactionMap[categoryName] = categoryExpenseSum

                        totalExpenseByCategories += categoryExpenseSum


                        addTransactionBriefView(linearLayout,categoryName,categoryExpenseSum,cat.category.iconID,cat.category.colorID)

                    }


                    setupPieChart(pieChart,totalExpenseByCategories.toString(), "By Category")
                    loadPieChartData(pieChart, "categories", transactionMap,colors)

                }
            }



            // BY ACCOUNT EXPENSES
            reportFragmentViewModel.getExpensesByAccount { accountExpenses ->

                if (accountExpenses != null) {

                    val transactionMap : MutableMap<String, BigDecimal> = sortedMapOf()
                    var totalExpenseByAccounts = BigDecimal.ZERO
                    val colors = ArrayList<Int>()
                    val pieChart = binding.fragmentReportsPieChartByAccount.byAccountPieChartTemplate
                    val linearLayout = binding.fragmentReportsPieChartByAccount.byAccountPieChartLinearLayout

                    clearTransactionBriefViews(linearLayout,pieChart)

                    for( acc in accountExpenses){

                        val accountName = acc.account.name
                        val accountExpenseSum  = acc.expensesByAccount.sumOf { it.amount }
                        if(accountName == SmartExpensesLocalDatabase.OTHERS || accountExpenseSum.toDouble() <= 0)
                            continue

                        colors.add(acc.account.iconColorID)
                        transactionMap[accountName] = accountExpenseSum

                        totalExpenseByAccounts += accountExpenseSum


                        addTransactionBriefView(linearLayout,accountName,accountExpenseSum,acc.account.iconID,acc.account.iconColorID)

                    }

                    setupPieChart(pieChart,totalExpenseByAccounts.toString(), "By Account")
                    loadPieChartData(pieChart, "accounts", transactionMap,colors)

                }

            }


        }

    }



    // Defines only the look of the pie chart
    private fun setupPieChart(pieChart: PieChart, title: String, desc: String) {

        pieChart.isDrawHoleEnabled = true
        pieChart.setUsePercentValues(true)
        pieChart.setEntryLabelTextSize(12f)
        pieChart.setEntryLabelColor(Color.BLACK)

        pieChart.centerText = title
        pieChart.setCenterTextSize(24f)

        pieChart.description.isEnabled = true
        pieChart.description.text = desc
        pieChart.legend.isEnabled = false
    }



    // Loads the data for pie chart
    private fun loadPieChartData(pieChart: PieChart, labelName: String, transactionMap : Map<String,BigDecimal>, colors: ArrayList<Int>) {
        val entries = ArrayList<PieEntry>()

        for(key in transactionMap.keys){

            val pieEntry = PieEntry(transactionMap[key]!!.toFloat(),key)
            entries.add(pieEntry)

        }

        val dataSet = PieDataSet(entries, labelName)
        dataSet.colors = colors
        dataSet.sliceSpace = 3f
        dataSet.valueTextSize = 18f

        dataSet.setDrawValues(false)

        val data = PieData(dataSet)
        pieChart.data = data
        pieChart.invalidate() // refresh
    }

    private fun addTransactionBriefView(layout: LinearLayout,name: String, sum: BigDecimal, iconId: Int, colorId: Int){


        val newTransactionView = requireActivity().layoutInflater.inflate(R.layout.pie_chart_transaction_brief, null)

        val nameView = newTransactionView.findViewById<TextView>(R.id.pie_chart_transaction_brief_view_name)
        val sumView = newTransactionView.findViewById<TextView>(R.id.pie_chart_transaction_brief_view_transaction_sum)
        val imageView = newTransactionView.findViewById<ImageView>(R.id.pie_chart_transaction_brief_view_image_view)

        nameView.text = name
        sumView.text = sum.toString()

        imageView.setImageResource(iconId)
        imageView.setBackgroundColor(colorId)


        layout.addView(newTransactionView)


    }

    private fun clearTransactionBriefViews(linearLayout: LinearLayout, viewToKeep: View){
        for (i in  linearLayout.childCount - 1 downTo 0) {
            val child = linearLayout.getChildAt(i)
            if (child.id != viewToKeep.id) {
                linearLayout.removeViewAt(i)
            }
        }
    }


}