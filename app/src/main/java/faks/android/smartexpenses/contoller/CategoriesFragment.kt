package faks.android.smartexpenses.contoller


import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.view.marginBottom
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import faks.android.smartexpenses.R
import faks.android.smartexpenses.databinding.FragmentCategoriesBinding
import faks.android.smartexpenses.misc.setupImageWithBorder
import faks.android.smartexpenses.model.*
import faks.android.smartexpenses.viewmodel.CategoryFragmentViewModel


// TODO: optimize and clean up this entire class

class CategoriesFragment : Fragment() {






    private var _binding: FragmentCategoriesBinding? = null
    private val binding get() = _binding!!


    private var incomeCategoriesSelected : Boolean = false
    private var expenseCategoriesSelected : Boolean = true



    private lateinit var categoryViewModel: CategoryFragmentViewModel


    companion object{
        const val INCOME: String = "Income"
        const val EXPENSE: String = "Expense"
        const val COLOR: String = "color"
        const val IMAGE: String = "image"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentCategoriesBinding.inflate(inflater, container, false)


        val mainFloatingButton = binding.categoryFragmentMainFloatingButton
        val listIncomeCategoriesButton = binding.listIncomeCategoriesButton
        val listExpenseCategoriesButton = binding.listExpenseCategoriesButton


        categoryViewModel = ViewModelProvider(this)[CategoryFragmentViewModel::class.java]

        listCategoriesByType()


        mainFloatingButton.setOnClickListener {
            activity?.let {

                val builder = AlertDialog.Builder(it)
                val customLayout = inflater.inflate(R.layout.add_category_popup_window_layout, null)



                //category data
                val categoryName = customLayout.findViewById<EditText>(R.id.add_category_name_edit_text)
                val openIconGrid = customLayout.findViewById<TextView>(R.id.add_category_icon_text_view)
                val openColorGrid = customLayout.findViewById<TextView>(R.id.add_category_icon_color_text_view)
                val displayIconImageView = customLayout.findViewById<ImageView>(R.id.add_category_display_icon_image_view)
                var newCategoryIconId : Int = -1
                var newCategoryColorID: Int = R.color.bisque

                // create grid view that displays icons

                val images = getIconsBy(getSelectedType())

                val iconPickerGridView = GridView(it)
                iconPickerGridView.numColumns = 5
                iconPickerGridView.horizontalSpacing = 8
                iconPickerGridView.verticalSpacing = 8
                iconPickerGridView.setPadding(16, 16, 16, 16)
                val adapter = CategoryIconImageAdapter(it, images)
                iconPickerGridView.adapter = adapter

                val iconPickerAlertDialog = AlertDialog.Builder(it)
                    .setView(iconPickerGridView)
                    .setPositiveButton("Close", null)
                    .create()

                //TODO somehow pass displayIconImageView trough imageViewExtension (change border and shape)
                iconPickerGridView.setOnItemClickListener { _, _, position, _ ->
                    // Get the selected image ID and pass it to the MainActivity
                    val selectedImageId = adapter.getItem(position) ?: 0
                    displayIconImageView.setImageResource(selectedImageId)
                    newCategoryIconId = selectedImageId
                    iconPickerAlertDialog.dismiss() // Dismiss the dialog after selecting an image
                }


                openIconGrid.setOnClickListener{
                    iconPickerAlertDialog.show()
                }

                val colors = getColors()

                val colorIconPickerGridView = GridView(it)
                colorIconPickerGridView.numColumns = 5
                colorIconPickerGridView.horizontalSpacing = 8
                colorIconPickerGridView.verticalSpacing = 8

                colorIconPickerGridView.adapter = CategoryIconColorAdapter(it, colors)

                val colorPickerAlertDialog = AlertDialog.Builder(it)
                    .setView(colorIconPickerGridView)
                    .setPositiveButton("Close", null)
                    .create()

                colorIconPickerGridView.setOnItemClickListener { _, _, position, _ ->
                    val selectedColor = colors[position]
                    // Handle the color selection
                    displayIconImageView.setBackgroundColor(selectedColor)


                    newCategoryColorID = selectedColor
                    colorPickerAlertDialog.dismiss()
                }


                openColorGrid.setOnClickListener {
                    colorPickerAlertDialog.show()
                }


                builder.setView(customLayout)
                    .setPositiveButton("Add") { _, _ ->

                        val name = categoryName.text.toString()
                        val type = getSelectedType()

                        categoryViewModel.getCategoryByName(name){category ->
                            category ?: run {
                                if(newCategoryIconId != -1) {

                                    val newCategory =
                                        Category(name, type, newCategoryIconId, newCategoryColorID)
                                    categoryViewModel.insertCategory(newCategory)
                                    clearCategoriesFromLayout()
                                    listCategoriesByType()

                                }else{
                                    Toast.makeText(
                                        requireContext(),
                                        "Did not select image id",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }

                            }

                            if(category != null){
                                Toast.makeText(
                                    requireContext(),
                                    "Category with that name already exists",
                                    Toast.LENGTH_LONG
                                ).show()
                            }

                        }


                    }
                    .setNegativeButton("Exit") { _, _ -> }

                val dialog = builder.create()
                dialog.show()
            }
        }


        listExpenseCategoriesButton.setOnClickListener {

            clearCategoriesFromLayout()

            incomeCategoriesSelected = false
            expenseCategoriesSelected = true

            listCategoriesByType()

        }

        listIncomeCategoriesButton.setOnClickListener {

            clearCategoriesFromLayout()

            incomeCategoriesSelected = true
            expenseCategoriesSelected = false

            listCategoriesByType()

        }



        return binding.root

    }


    // TODO add more icons and possibly add them dynamically so we do not have huge functions
    private fun getIconsBy( type : String) : Array<Int>{

        if(type == EXPENSE) {
            return arrayOf(
                R.drawable.paint_pallete,
                R.drawable.family,
                R.drawable.cheers,
                R.drawable.credit_card,
                R.drawable.home,
                R.drawable.pawprint,
                R.drawable.car_icon,
                R.drawable.restaurant,
                R.drawable.shopping_cart,
                R.drawable.sunbed,
                R.drawable.web
            )
        }
        else{

            return arrayOf(
                R.drawable.bank,
                R.drawable.id_card,
                R.drawable.profit,
                R.drawable.portfolio,
                R.drawable.folder
            )

        }

    }

    private fun getSelectedType() : String {

        return if (incomeCategoriesSelected)
            INCOME
        else
            EXPENSE

    }

    // TODO add margin bottom to this view and perhaps change its background color so its more visible.
    private fun addCategoryBriefView(category: Category){

        // create a new category view dynamically
        val newCategoryView = requireActivity().layoutInflater.inflate(R.layout.category_brief_view, null)

        // set category name in view
        val categoryName = newCategoryView.findViewById<TextView>(R.id.category_name_text_view)
        categoryName.text = category.name

        val categoryIcon = newCategoryView.findViewById<ImageView>(R.id.category_icon_image_view)

        categoryIcon.setupImageWithBorder(
            iconId = category.iconID,
            colorId = category.colorID,
            radius = 3,
            margin = 3,
            borderWidth = 3

        )


        val deleteCategoryImageView = newCategoryView.findViewById<ImageView>(R.id.delete_category_image_view)

        deleteCategoryImageView.setOnClickListener{
            deleteCategoryPopup(category)
        }


        binding.categoryLinearLayoutContainer.addView(newCategoryView)


    }

    private fun listCategoriesByType(){

        categoryViewModel.getCategoriesByType(getSelectedType()){categories ->

            for(category in categories){

                if(category.name == SmartExpensesLocalDatabase.DEFAULT_CATEGORY_INCOME
                    || category.name == SmartExpensesLocalDatabase.DEFAULT_CATEGORY_EXPENSE)
                    continue

                addCategoryBriefView(category)
            }

        }

    }

    private fun clearCategoriesFromLayout(){

        val linearLayout = binding.categoryLinearLayoutContainer

        if(linearLayout.childCount > 0)
            linearLayout.removeAllViews()

    }


    private fun deleteCategoryPopup(category: Category){

        val builder = AlertDialog.Builder(requireContext())

        builder.setTitle("Confirm")
        builder.setMessage("Are you sure you want to delete this category?\n" +
                "All income and expenses will have their categories changed to 'default_category'")

        builder.setPositiveButton("Yes") { _, _ ->

            transferTransactionAccountToDefaultCategory(category)
            categoryViewModel.deleteCategory(category)
            clearCategoriesFromLayout()
            listCategoriesByType()
        }

        builder.setNegativeButton("No") { _, _ ->

        }

        val dialog = builder.create()
        dialog.show()

    }


    // When deleting category all incomes and expenses with that category will be defaulted to default category
    private fun transferTransactionAccountToDefaultCategory(category: Category){

        categoryViewModel.getTransactionsByCategory(category.name){ incomes, expenses ->

            for(income in incomes){

                val newIncome = Income(
                    income.incomeID,income.amount, income.accountID,
                    SmartExpensesLocalDatabase.DEFAULT_CATEGORY_INCOME, income.creationTime, income.description
                )

                categoryViewModel.updateIncome(newIncome)

            }

            for(expense in expenses){

                val newExpense = Expense(
                    expense.expenseID,expense.amount, expense.accountID,
                    SmartExpensesLocalDatabase.DEFAULT_CATEGORY_EXPENSE, expense.creationTime, expense.description
                )

                categoryViewModel.updateExpense(newExpense)

            }

        }

    }

    private fun getColors(): Array<Int> {

        val colorArray = requireActivity().resources.obtainTypedArray(R.array.my_colors)
        val colorList = mutableListOf<Int>()

        for (i in 0 until colorArray.length()) {
            colorList.add(colorArray.getColor(i, 0))
        }

        colorArray.recycle() // Remember to recycle the TypedArray

        return colorList.toTypedArray()
    }


    private fun updateImageView(imageView: ImageView,color: Int?,icon: Int?){

        val iconId = icon ?: R.drawable.trash_can_icon
        val colorId = icon ?: Color.WHITE

        imageView.setupImageWithBorder(
            iconId = iconId,
            colorId = colorId,
            radius = 2,
            margin = 2,
            borderWidth = 2
            // borderColor = Color.BLACK (optional, default is black)
        )




    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



}