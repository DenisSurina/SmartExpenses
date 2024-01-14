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
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.squareup.picasso.Picasso
import faks.android.smartexpenses.R
import faks.android.smartexpenses.databinding.FragmentHomeBinding
import faks.android.smartexpenses.model.Account
import faks.android.smartexpenses.model.Expense
import faks.android.smartexpenses.model.Income
import faks.android.smartexpenses.model.IncomeExpenseWrapper
import faks.android.smartexpenses.viewmodel.HomeFragmentViewModel
import java.math.BigDecimal
import java.text.SimpleDateFormat
import java.util.*


class HomeFragment : Fragment() {


    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    companion object {
        const val EXPENSE_CATEGORY = 1
        const val INCOME_CATEGORY = 2
        const val ACCOUNT = 3
        const val INCOME = "Income"
        const val EXPENSE = "Expense"
        const val DATE_FORMAT = "dd/MM/yyyy"
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

        setupMainTransactionView()


        displayTransactions()


        return binding.root
    }

    override fun onResume() {
        super.onResume()
        setupMainTransactionView()
    }

    // This lists ALL expenses, incomes and total balance on TOP VIEW on home fragment
    private fun setupMainTransactionView(){


        homeFragmentViewModel.getExpenses { expenses ->
            val expensesSum = expenses.map { expense: Expense ->  expense.amount}.sum()
            binding.includedTotalBalanceBriefViewLayout.totalExpenseBriefDisplayTextView.text = "€$expensesSum"
        }

        homeFragmentViewModel.getIncomes { incomes ->
            val incomeSum = incomes.map { income: Income ->  income.amount}.sum()
            binding.includedTotalBalanceBriefViewLayout.totalIncomeBriefDisplayTextView.text = "€$incomeSum"
        }

        homeFragmentViewModel.getAccounts { accounts ->

            val totalBalance = accounts.map { account: Account -> account.balance }.sum()
            binding.includedTotalBalanceBriefViewLayout.totalBalanceTextView.text = "€$totalBalance"

        }


    }



    private fun setupAddIncomeWindow(){


        activity?.let {
            val builder = AlertDialog.Builder(it)
            val customLayout = it.layoutInflater.inflate(R.layout.add_income_popup_window_layout, null)

            val incomeCategoryTextView = customLayout.findViewById<TextView>(R.id.addIncomePopupWindowSetCategoryButton)
            incomeCategoryTextView.setOnClickListener {
                listTransactionItems(INCOME_CATEGORY){ name ->
                    incomeCategoryTextView.text = name
                }
            }

            val incomeAccountTextView = customLayout.findViewById<TextView>(R.id.addIncomePopupWindowSetAccountButton)
            incomeAccountTextView.setOnClickListener {
                listTransactionItems(ACCOUNT){name ->
                    incomeAccountTextView.text = name
                }
            }

            val openDatePicker = customLayout.findViewById<TextView>(R.id.addIncomePopupWindowSetDateButton)
            openDatePicker.setOnClickListener {
                openDatePickerDialog(openDatePicker)
            }

            val amountEditText = customLayout.findViewById<EditText>(R.id.addIncomePopupWindowAmountTextView).text
            val descriptionEditText = customLayout.findViewById<EditText>(R.id.AddIncomePopupWindowSetDescriptionTextView).text


            builder.setView(customLayout)
                .setPositiveButton("Add") { _, _ ->

                    val errors = StringBuilder()

                    if(amountEditText.isBlank())
                        errors.append("Amount cannot be empty.\n")

                    if(incomeCategoryTextView.text.isBlank())
                        errors.append("Category must be selected\n")

                    if(incomeAccountTextView.text.isBlank())
                        errors.append("Account must be selected\n")

                    if(openDatePicker.text.isBlank())
                        errors.append("Date must be selected\n")


                    if(errors.isBlank()){


                        val amount = BigDecimal(amountEditText.toString())
                        val category = incomeCategoryTextView.text.toString()
                        val accountName = incomeAccountTextView.text.toString()
                        val date = parseDate(openDatePicker.text.toString())!!
                        val description = descriptionEditText.toString()

                        val newIncome = Income(amount = amount, categoryName = category, accountID = accountName, description = description, creationTime = date)

                        homeFragmentViewModel.getAccountByName(accountName){ account ->

                            val newBalance = account.balance.add(amount)

                            val newAccount = Account(account.name,account.iconID,account.iconColorID,newBalance)

                            homeFragmentViewModel.updateAccount(newAccount)
                            homeFragmentViewModel.insertIncome(newIncome)

                            Toast.makeText(requireContext(),"Added new income", Toast.LENGTH_SHORT).show()
                            displayTransactions()
                            setupMainTransactionView()
                        }


                    }else{
                        Toast.makeText(requireContext(),errors, Toast.LENGTH_LONG).show()
                    }


                }


            val dialog = builder.create()
            dialog.show()
        }


    }

    //This function sets up alert window for Expense creation
    private fun setupAddExpenseWindow(){
        activity?.let {
            val builder = AlertDialog.Builder(it)
            val customLayout = it.layoutInflater.inflate(R.layout.add_expense_popup_window_layout, null)


            val expenseCategoryTextView = customLayout.findViewById<TextView>(R.id.addExpensePopupWindowSetCategoryButton)
            expenseCategoryTextView.setOnClickListener {
                listTransactionItems(EXPENSE_CATEGORY){name ->
                    expenseCategoryTextView.text = name
                }
            }

            val expenseAccountTextView = customLayout.findViewById<TextView>(R.id.addExpensePopupWindowSetAccountButton)
            expenseAccountTextView.setOnClickListener {
                listTransactionItems(ACCOUNT){name ->
                    expenseAccountTextView.text = name
                }
            }

            val openDatePicker = customLayout.findViewById<TextView>(R.id.addExpensePopupWindowSetDateButton)
            openDatePicker.setOnClickListener {
                openDatePickerDialog(openDatePicker)
            }

            val amountEditText = customLayout.findViewById<EditText>(R.id.addExpensePopupWindowAmountTextView).text
            val descriptionEditText = customLayout.findViewById<EditText>(R.id.AddExpensePopupWindowSetDescriptionTextView).text

            builder.setView(customLayout)
                .setPositiveButton("Add") { _, _ ->

                    val errors = StringBuilder()
                    
                    if(amountEditText.isBlank())
                        errors.append("Amount cannot be empty.\n")
                    
                    if(expenseCategoryTextView.text.isBlank())
                        errors.append("Category must be selected\n")

                    if(expenseAccountTextView.text.isBlank())
                        errors.append("Account must be selected\n")

                    if(openDatePicker.text.isBlank())
                        errors.append("Date must be selected\n")


                    if(errors.isBlank()){


                        val amount = BigDecimal(amountEditText.toString())
                        val category = expenseCategoryTextView.text.toString()
                        val accountName = expenseAccountTextView.text.toString()
                        val date = parseDate(openDatePicker.text.toString())!!
                        val description = descriptionEditText.toString()

                        val newExpense = Expense(amount = amount, categoryName = category, accountID = accountName, description = description, creationTime = date)

                        homeFragmentViewModel.getAccountByName(accountName){ account ->

                            val newBalance = account.balance.subtract(amount)

                            val newAccount = Account(account.name,account.iconID,account.iconColorID,newBalance)

                            homeFragmentViewModel.updateAccount(newAccount)
                            homeFragmentViewModel.insertExpense(newExpense)

                            Toast.makeText(requireContext(),"Added new expense", Toast.LENGTH_SHORT).show()
                            displayTransactions()
                            setupMainTransactionView()
                        }


                    }else{
                        Toast.makeText(requireContext(),errors, Toast.LENGTH_LONG).show()
                    }

                }


            val dialog = builder.create()
            dialog.show()
        }
    }

    //This functions sets up item_linear_layout do display items such as categories and accounts
    //Here a user can select category and account when creating new expenses or incomes
    private fun listTransactionItems( choice : Int, onItemSelected: (String) -> Unit){

        activity?.let {
            val builder = AlertDialog.Builder(it)
            val customLayout = it.layoutInflater.inflate(R.layout.item_linear_layout, null) as LinearLayout
            builder.setView(customLayout)
            val dialog = builder.create()

            when (choice) {
                EXPENSE_CATEGORY -> {

                    homeFragmentViewModel.getCategoriesByType(EXPENSE){categories ->

                        for(category in categories){
                            val item = createItemForLinearLayout(category.iconID,category.colorID,category.name)
                            item?.setOnClickListener {
                                dialog.dismiss()
                                onItemSelected(category.name)
                            }
                            customLayout.addView(item)
                        }
                    }

                }
                INCOME_CATEGORY -> {

                    homeFragmentViewModel.getCategoriesByType(INCOME){categories ->

                        for(category in categories){
                            val item = createItemForLinearLayout(category.iconID,category.colorID,category.name)
                            item?.setOnClickListener {
                                dialog.dismiss()
                                onItemSelected(category.name)
                            }
                            customLayout.addView(item)
                        }
                    }

                }
                ACCOUNT -> {

                    homeFragmentViewModel.getAccounts{accounts ->

                        for(account in accounts){
                            val item = createItemForLinearLayout(account.iconID,account.iconColorID,account.name)
                            item?.setOnClickListener {
                                dialog.dismiss()
                                onItemSelected(account.name)
                            }
                            customLayout.addView(item)
                        }
                    }

                }
            }

            dialog.show()
        }
    }


    // This function creates items (categories,accounts) to be displayed in item_linear_layout
    private fun createItemForLinearLayout(iconId: Int, colorId: Int, name: String) : View?{

        val item = requireActivity().layoutInflater.inflate(R.layout.item_brief_view,null)
        val itemImage = item.findViewById<ImageView>(R.id.item_image_view_brief)
        val itemName = item.findViewById<TextView>(R.id.item_text_view_brief)

        Picasso.get()
            .load(iconId)
            .transform(ImageTransformer(0, 0))
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


    private fun openDatePickerDialog(textView: TextView) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this.requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->

                val selectedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                textView.text = selectedDate
            },
            year, month, day
        )

        datePickerDialog.show()
    }

    private fun parseDate(dateString: String): Date? {
        val format = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())
        return try {
            format.parse(dateString)
        } catch (e: Exception) {
            null  // Return null if the parsing fails
        }
    }



    // Lists the sum of ALL transaction (expenses, incomes) by day on home fragment
    private fun displayTransactions(){

        homeFragmentViewModel.getIncomeExpenseWrapperMapByDateString(DATE_FORMAT) { wrappers->


            val includedViewId = R.id.includedTotalBalanceBriefViewLayout

            for (i in  binding.linearLayoutContainer.childCount - 1 downTo 0) {
                val child = binding.linearLayoutContainer.getChildAt(i)
                if (child.id != includedViewId) {
                    binding.linearLayoutContainer.removeViewAt(i)
                }
            }

            val wrappersSorted = wrappers.toSortedMap()

            for(key in wrappersSorted.keys){

                val date = SimpleDateFormat(DATE_FORMAT, Locale.getDefault()).parse(key)

                val incomes = wrappersSorted.getValue(key).filter { it.type == IncomeExpenseWrapper.INCOME }
                val expenses = wrappersSorted.getValue(key).filter { it.type == IncomeExpenseWrapper.EXPENSE }

                val sumOfIncomes = incomes.map { v -> v.amount }.sum()
                val sumOfExpenses = expenses.map { v -> v.amount }.sum()


                if (date != null) {
                    addTransactionSingleView(date,sumOfIncomes,sumOfExpenses)
                }

            }

        }

    }

    private fun addTransactionSingleView(date: Date,incomeSum: BigDecimal, expenseSum: BigDecimal ){

        val transactionView = requireActivity().layoutInflater.inflate(R.layout.transaction_brief_view,null)
        val transactionDayNumber = transactionView.findViewById<TextView>(R.id.day_of_the_month_text_view)
        val transactionDayText = transactionView.findViewById<TextView>(R.id.day_of_the_week_and_month_text_view)
        val incomeTextView = transactionView.findViewById<TextView>(R.id.income_text_view)
        val expenseTextView = transactionView.findViewById<TextView>(R.id.expense_text_view)

        transactionDayNumber.text = date?.date.toString()
        val transactionDayFormat = formatTransactionDateString(date!!)
        transactionDayText.text = transactionDayFormat
        incomeTextView.text = "€$incomeSum"
        expenseTextView.text = "€$expenseSum"

        binding.linearLayoutContainer.addView(transactionView)
    }


    private fun formatTransactionDateString(date: Date): String{

        val dayOfTheWeek = getDayOfTheWeek(date)
        val month = getMonth(date)
        val year = date.year + 1900

        return "$dayOfTheWeek\n$month. $year"

    }

    private fun getDayOfTheWeek(date: Date): String{
        return when (date.day) {
            1 -> "Monday"
            2 -> "Tuesday"
            3 -> "Wednesday"
            4 -> "Thursday"
            5 -> "Friday"
            6 -> "Saturday"
            else -> "Sunday"
        }
    }

    private fun getMonth(date: Date): String{
        return when (date.month) {
            1 -> "Feb"
            2 -> "March"
            3 -> "May"
            5 -> "June"
            6 -> "July"
            7 -> "Aug"
            8 -> "Sep"
            9 -> "Oct"
            10 -> "Nov"
            11 -> "Dec"
            else -> "Jan"
        }
    }

    private fun Iterable<BigDecimal>.sum(): BigDecimal {
        var sum: BigDecimal = BigDecimal.ZERO
        for (element in this) {
            sum += element
        }
        return sum
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }




}