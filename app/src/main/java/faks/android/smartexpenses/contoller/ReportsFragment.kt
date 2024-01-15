package faks.android.smartexpenses.contoller

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import faks.android.smartexpenses.R
import faks.android.smartexpenses.databinding.FragmentReportsBinding

class ReportsFragment : Fragment() {


    private var _binding: FragmentReportsBinding? = null
    private val binding get() = _binding!!

    private var expenseSelected = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentReportsBinding.inflate(inflater, container, false)

        setupButtons()

        val pieChart = binding.pieChartCategory.pieChartTemplate
        setupPieChart(pieChart)
        loadPieChartData(pieChart)

        // Return the root of the binding
        return binding.root
    }


    private fun setupButtons(){
        val showExpenseGraphs: Button = binding.showExpenseGraphs
        val showIncomeGraphs: Button = binding.showIncomeGraphs

        showExpenseGraphs.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.cyan))
        showIncomeGraphs.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.blue))

        showExpenseGraphs.setOnClickListener {

            changeButtonColor(showExpenseGraphs, true)
            changeButtonColor(showIncomeGraphs, false)
        }

        showIncomeGraphs.setOnClickListener {

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

    private fun setupPieChart(pieChart: PieChart) {
        // Configure pie chart appearance
        pieChart.isDrawHoleEnabled = true
        pieChart.setUsePercentValues(true)
        pieChart.setEntryLabelTextSize(12f)
        pieChart.setEntryLabelColor(Color.BLACK)
        pieChart.centerText = "Distribution"
        pieChart.setCenterTextSize(24f)
        // Configure legend
        val legend = pieChart.legend
        legend.verticalAlignment = Legend.LegendVerticalAlignment.TOP
        legend.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
        legend.orientation = Legend.LegendOrientation.VERTICAL
        legend.setDrawInside(false)
        legend.isEnabled = true
    }

    private fun loadPieChartData(pieChart: PieChart) {
        val entries = ArrayList<PieEntry>()
        // TODO: Replace this with your data
        entries.add(PieEntry(30.0f, "Category A"))
        entries.add(PieEntry(20.0f, "Category B"))
        entries.add(PieEntry(50.0f, "Category C"))

        val dataSet = PieDataSet(entries, "Categories")
        // TODO: Customize colors
        dataSet.setColors(*ColorTemplate.MATERIAL_COLORS)
        dataSet.sliceSpace = 3f
        dataSet.valueTextSize = 18f

        val data = PieData(dataSet)
        pieChart.data = data
        pieChart.invalidate() // refresh
    }

}