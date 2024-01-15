package faks.android.smartexpenses.contoller

import android.content.res.ColorStateList
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
}