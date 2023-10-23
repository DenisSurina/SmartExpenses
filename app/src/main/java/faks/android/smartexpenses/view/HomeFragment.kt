package faks.android.smartexpenses.view


import android.content.DialogInterface
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.PopupWindow
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import faks.android.smartexpenses.R
import faks.android.smartexpenses.databinding.FragmentHomeBinding
import java.util.*


class HomeFragment : Fragment() {


    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var popupWindow: PopupWindow
    private val calendar: Calendar = Calendar.getInstance()

    // Floating Button Animations
    private val rotateOpen : Animation by lazy { AnimationUtils.loadAnimation(requireContext(),R.anim.rotate_open_anim) }
    private val rotateClose : Animation by lazy { AnimationUtils.loadAnimation(requireContext(),R.anim.rotate_close_anim) }
    private val fromBottom : Animation by lazy { AnimationUtils.loadAnimation(requireContext(),R.anim.from_bottom_anim) }
    private val toBottom : Animation by lazy { AnimationUtils.loadAnimation(requireContext(),R.anim.to_bottom_anim) }


    //boolean to check if main floating action but is clicked or not
    private var clicked : Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)


        val mainFloatingButton    =  binding.homeFragmentMainFloatingButton
        val incomeFloatingButton  =  binding.homeFragmentAddIncomeButton
        val expenseFloatingButton =  binding.homeFragmentAddExpenseButton


        incomeFloatingButton.setOnClickListener{
            Toast.makeText(requireContext(),"Adding Income", Toast.LENGTH_SHORT).show()
        }

        expenseFloatingButton.setOnClickListener{
            Toast.makeText(requireContext(),"Adding Expense", Toast.LENGTH_SHORT).show()
        }

        mainFloatingButton.setOnClickListener {
            showFloatingActionButtons()
        }


        /*
        mainFloatingButton.setOnClickListener { view ->
            activity?.let {
                val builder = AlertDialog.Builder(it)

                builder.setView(faks.android.smartexpenses.R.layout.popup_window_custom_layout)
                    // Add action buttons
                    .setPositiveButton("Add") { dialog, id ->
                        testFunction(dialog)
                    }
                    .setNegativeButton("Exit",
                        DialogInterface.OnClickListener { dialog, id ->

                        })
                val dialog = builder.create()
                dialog.show()
            }
        }

        */


        return binding.root
    }



    private fun showFloatingActionButtons(){

        toggleButtonVisibility()
        toggleButtonAnimation()
        clicked = !clicked

    }

    private fun toggleButtonVisibility(){
        if(!clicked){
            binding.homeFragmentAddExpenseButton.visibility = View.VISIBLE
            binding.homeFragmentAddIncomeButton.visibility = View.VISIBLE
        }else{
            binding.homeFragmentAddExpenseButton.visibility = View.INVISIBLE
            binding.homeFragmentAddIncomeButton.visibility = View.INVISIBLE
        }
    }

    private fun toggleButtonAnimation(){
        if(!clicked){
            binding.homeFragmentAddExpenseButton.startAnimation(fromBottom)
            binding.homeFragmentAddIncomeButton.startAnimation(fromBottom)
            binding.homeFragmentMainFloatingButton.startAnimation(rotateOpen)
        }else{
            binding.homeFragmentAddExpenseButton.startAnimation(toBottom)
            binding.homeFragmentAddIncomeButton.startAnimation(toBottom)
            binding.homeFragmentMainFloatingButton.startAnimation(rotateClose)
        }
    }


    /*
    helper function for adding new expense_income_view
     */
    private fun testFunction( dialog : DialogInterface){


/*
        val customViewTwo = requireActivity().layoutInflater.inflate(R.layout.transaction_brief_view, null)


        val dayOfTheMonthTwo = customViewTwo.findViewById<TextView>(R.id.day_of_the_month_text_view)
        val dayOfTheWeekAndMonthTwo = customViewTwo.findViewById<TextView>(R.id.day_of_the_week_and_month_text_view)
        val incomeTwo = customViewTwo.findViewById<TextView>(R.id.income_text_view)
        val expenseTwo = customViewTwo.findViewById<TextView>(R.id.expense_text_view)

        dayOfTheMonthTwo.text = calendar.get(Calendar.DAY_OF_MONTH).toString()

        val dayOfWeekTwo = getDayOfWeekName(calendar.get(Calendar.DAY_OF_WEEK))
        val monthNameTwo = getMonthName(calendar.get(Calendar.MONTH))
        val textTwo = getString(R.string.day_month_format, dayOfWeekTwo, monthNameTwo)
        dayOfTheWeekAndMonthTwo.text = textTwo

        incomeTwo.text = "500"
        expenseTwo.text = "500"

        binding.linearLayoutContainer.addView(customViewTwo)

 */
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun getDayOfWeekName(dayOfWeek: Int): String {
        val daysOfWeek = arrayOf("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday")
        return daysOfWeek[dayOfWeek - 1]
    }



    private fun getMonthName(month: Int): String {
        val months = arrayOf(
            "January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"
        )
        return months[month]
    }



    //#########################################
    //Possibly delete this
    //#########################################
    //floating action button on click action
    private fun showSmallActionButtons() {
        // Inflate the layout for the small action buttons
        val inflater = LayoutInflater.from(requireContext())
        val smallButtonsView = inflater.inflate(R.layout.layout_small_buttons, null)

        // Create a PopupWindow to display the small action buttons
        popupWindow = PopupWindow(
            smallButtonsView,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        )

        // Show the PopupWindow above the main FloatingActionButton
        val addNewTransactionButton = binding.homeFragmentMainFloatingButton
        popupWindow.showAsDropDown(addNewTransactionButton, 0, -addNewTransactionButton.height)

        // Handle click events for each small button
        val button1 = smallButtonsView.findViewById<Button>(R.id.button1)
        button1.setOnClickListener {
            // Show the popup window for button 1
            showPopupWindowForButton1()
        }

        // ... Repeat the above process for the other buttons ...
    }

    private fun showPopupWindowForButton1() {
        // Inflate the layout for the popup window content
        val inflater = LayoutInflater.from(requireContext())
        val popupContentView = inflater.inflate(R.layout.popup_window_layout, null)

        // Create a PopupWindow for the popup window content
        val popup = PopupWindow(
            popupContentView,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        )

        // Show the PopupWindow in the center of the screen
        val parentView = requireView()
        popup.showAtLocation(parentView, Gravity.CENTER, 0, 0)
    }


}