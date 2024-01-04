package faks.android.smartexpenses.contoller



import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.squareup.picasso.Picasso
import faks.android.smartexpenses.R
import faks.android.smartexpenses.databinding.FragmentHomeBinding
import faks.android.smartexpenses.viewmodel.AccountManagementActivityViewModel
import faks.android.smartexpenses.viewmodel.HomeFragmentViewModel
import java.util.*
import kotlin.collections.HashMap


class HomeFragment : Fragment() {


    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    companion object {
        const val EXPENSE_CATEGORY = 1
        const val INCOME_CATEGORY = 2
        const val ACCOUNT = 3
        const val INCOME = "Income"
        const val EXPENSE = "Expense"
    }



    // Floating Button Animations
    private val rotateOpen : Animation by lazy { AnimationUtils.loadAnimation(requireContext(),R.anim.rotate_open_anim) }
    private val rotateClose : Animation by lazy { AnimationUtils.loadAnimation(requireContext(),R.anim.rotate_close_anim) }
    private val fromBottom : Animation by lazy { AnimationUtils.loadAnimation(requireContext(),R.anim.from_bottom_anim) }
    private val toBottom : Animation by lazy { AnimationUtils.loadAnimation(requireContext(),R.anim.to_bottom_anim) }


    //boolean to check if main floating action but is clicked or not
    private var clicked : Boolean = false


    private lateinit var homeFragmentViewModel: HomeFragmentViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        homeFragmentViewModel = ViewModelProvider(this)[HomeFragmentViewModel::class.java]

        val mainFloatingButton    =  binding.homeFragmentMainFloatingButton
        val incomeFloatingButton  =  binding.homeFragmentAddIncomeButton
        val expenseFloatingButton =  binding.homeFragmentAddExpenseButton


        incomeFloatingButton.setOnClickListener{
            setupAddIncomeWindow()
        }

        expenseFloatingButton.setOnClickListener{
            setupAddExpenseWindow()
        }

        mainFloatingButton.setOnClickListener {
            showFloatingActionButtons()
        }

        // set up a button that open account management activity
        val manageAccountButton : Button = binding.includedTotalBalanceBriefViewLayout.manageAccountsButton
        manageAccountButton.setOnClickListener {
            val intent = Intent(requireContext(), AccountManagementActivity::class.java)
            startActivity(intent)
        }


        return binding.root
    }



    private fun setupAddIncomeWindow(){


        activity?.let {
            val builder = AlertDialog.Builder(it)
            val customLayout = it.layoutInflater.inflate(R.layout.add_income_popup_window_layout, null)
            builder.setView(customLayout)
                // Add action buttons
                .setPositiveButton("Add") { _, _ ->


                }
                .setNegativeButton("Exit"
                ) { _, _ ->

                }

            val openDatePicker = customLayout.findViewById<TextView>(R.id.addIncomePopupWindowSetDateButton)
            openDatePicker.setOnClickListener {
                openDatePickerDialog()
            }
            val dialog = builder.create()
            dialog.show()
        }


    }


    private fun setupAddExpenseWindow(){
        activity?.let {
            val builder = AlertDialog.Builder(it)
            val customLayout = it.layoutInflater.inflate(R.layout.add_expense_popup_window_layout, null)


            val expenseCategoryTextView = customLayout.findViewById<TextView>(R.id.addExpensePopupWindowSetCategoryButton)

            expenseCategoryTextView.setOnClickListener {
                listTransactionItems(EXPENSE_CATEGORY)
            }

            builder.setView(customLayout)
                // Add action buttons
                .setPositiveButton("Add") { _, _ ->


                }
                .setNegativeButton("Exit"
                ) { _, _ ->

                }

            val openDatePicker = customLayout.findViewById<TextView>(R.id.addExpensePopupWindowSetDateButton)
            openDatePicker.setOnClickListener {
                openDatePickerDialog()
            }
            val dialog = builder.create()
            dialog.show()
        }
    }


    private fun listTransactionItems( choice : Int){

        activity?.let {
            val builder = AlertDialog.Builder(it)
            val customLayout = it.layoutInflater.inflate(R.layout.item_linear_layout, null) as LinearLayout

            if(choice == EXPENSE_CATEGORY){

                homeFragmentViewModel.getCategoriesByType(EXPENSE){categories ->

                    for(category in categories){

                        val item = createItemForLinearLayout(category.iconID,category.colorID,category.name)
                        customLayout.addView(item)
                    }
                }

            }

            builder.setView(customLayout)
                // Add action buttons
                .setPositiveButton("Add") { _, _ ->


                }
                .setNegativeButton("Exit"
                ) { _, _ ->

                }

            val dialog = builder.create()
            dialog.show()
        }

    }

    private fun createItemForLinearLayout(iconId: Int, colorId: Int, name: String) : View?{

        val item = activity?.layoutInflater?.inflate(R.layout.item_linear_layout,null)
        val itemImage = item?.findViewById<ImageView>(R.id.item_image_view_brief)
        val itemName = item?.findViewById<TextView>(R.id.item_text_view_brief)

        Picasso.get()
            .load(iconId)
            .transform(ImageTransformer(0, 10)) // 30px radius, 10px margin
            .into(itemImage)

        val roundedBackground = ContextCompat.getDrawable(requireContext(), R.drawable.icons_background) as GradientDrawable
        roundedBackground.setColor(colorId)
        itemImage?.background = roundedBackground

        itemName?.text = name

        return item

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


    private fun openDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this.requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->

                val selectedDate = "$selectedYear-${selectedMonth + 1}-$selectedDay" // Example format
                TODO("Somehow add this date to database")
            },
            year, month, day
        )

        datePickerDialog.show()
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }




}