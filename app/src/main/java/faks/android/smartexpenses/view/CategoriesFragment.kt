package faks.android.smartexpenses.view


import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.room.Room
import faks.android.smartexpenses.R
import faks.android.smartexpenses.databinding.FragmentCategoriesBinding
import faks.android.smartexpenses.model.Category
import faks.android.smartexpenses.model.CategoryDao
import faks.android.smartexpenses.model.SmartExpensesLocalDatabase

class CategoriesFragment : Fragment() {


    private var _binding: FragmentCategoriesBinding? = null
    private val binding get() = _binding!!


    private var incomeCategoriesSelected : Boolean = false
    private var expenseCategoriesSelected : Boolean = true


    private lateinit var categoryDao: CategoryDao

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentCategoriesBinding.inflate(inflater, container, false)


        val mainFloatingButton = binding.categoryFragmentMainFloatingButton
        val listIncomeCategoriesButton = binding.listIncomeCategoriesButton
        val listExpenseCategoriesButton = binding.listExpenseCategoriesButton


        val db = Room.databaseBuilder(
            requireContext(),
            SmartExpensesLocalDatabase::class.java, "main-db"
        ).allowMainThreadQueries().fallbackToDestructiveMigration().build()

        categoryDao = db.categoryDao()

        listCategoriesByType()





        mainFloatingButton.setOnClickListener {
            activity?.let {

                val builder = AlertDialog.Builder(it)
                val customLayout = inflater.inflate(R.layout.add_category_popup_window_layout, null)

                //category data
                val categoryName = customLayout.findViewById<EditText>(R.id.add_category_name_edit_text)
                val openIconGrid = customLayout.findViewById<TextView>(R.id.add_category_icon_text_view)
                val displayIconImageView = customLayout.findViewById<ImageView>(R.id.add_category_display_icon_image_view)
                var newCategoryIconId : Int = -1

                // create grid view that displays icons

                // Sample images (replace with your actual image resources)
                val images = arrayOf(
                    R.drawable.happy_emoji,
                    R.drawable.green_arrow_down,
                    R.drawable.floating_button_plus_sign,
                    R.drawable.paint_pallete,
                    R.drawable.green_arrow_up
                    // Add more image resources as needed
                )

                val gridView = GridView(it)
                gridView.numColumns = 5 // Set the number of columns as needed
                gridView.horizontalSpacing = 8 // Adjust horizontal spacing as needed
                gridView.verticalSpacing = 8 // Adjust vertical spacing as needed
                gridView.setPadding(16, 16, 16, 16)
                val adapter = CategoryIconImageAdapter(it, images)
                gridView.adapter = adapter



                val alertDialog = AlertDialog.Builder(it)
                    .setView(gridView)
                    .setPositiveButton("Close", null)
                    .create()

                gridView.setOnItemClickListener { _, _, position, _ ->
                    // Get the selected image ID and pass it to the MainActivity
                    val selectedImageId = adapter.getItem(position) ?: 0
                    displayIconImageView.setImageResource(selectedImageId)
                    newCategoryIconId = selectedImageId
                    alertDialog.dismiss() // Dismiss the dialog after selecting an image
                }


                openIconGrid.setOnClickListener{
                    alertDialog.show()
                }

                builder.setView(customLayout)
                    .setPositiveButton("Add") { _, _ ->

                        val name = categoryName.text.toString()
                        var mismatchName = false
                        val categories = categoryDao.getAll()
                        for(category in categories){
                            if (name == category.name){

                                mismatchName = true
                                break

                            }
                        }

                        if(!mismatchName && newCategoryIconId != -1){
                            val type = getSelectedType()
                            val newCategory = Category( name,type, newCategoryIconId)
                            categoryDao.insertAll(newCategory)
                            clearCategoriesFromLayout()
                            listCategoriesByType()
                        }else{
                            Toast.makeText(
                                    requireContext(),
                                    "Category with that name already exists",
                                    Toast.LENGTH_LONG
                                ).show()
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

    private fun createGridView( it : Activity, resources: Array<Int>) : GridView {

        val gridView = GridView(it)
        gridView.numColumns = 5
        gridView.horizontalSpacing = 8
        gridView.verticalSpacing = 8
        gridView.setPadding(16, 16, 16, 16)

        val adapter = CategoryIconImageAdapter(it, resources)
        gridView.adapter = adapter

        val alertDialog = AlertDialog.Builder(it)
            .setView(gridView)
            .setPositiveButton("Close", null)
            .create()
/*
        gridView.setOnItemClickListener { _, _, position, _ ->
            // Get the selected image ID and pass it to the MainActivity
            val selectedImageId = adapter.getItem(position) ?: 0
            displayIconImageView.setImageResource(selectedImageId)
            alertDialog.dismiss() // Dismiss the dialog after selecting an image
        }


        openIconGrid.setOnClickListener{
            alertDialog.show()
        }
        */
        return gridView
    }

    private fun getSelectedType() : String {

        return if (incomeCategoriesSelected)
            "Income"
        else
            "Expense"

    }


    private fun addCategoryBriefView(category: Category){

        // create a new category view dynamically
        val newCategoryView = requireActivity().layoutInflater.inflate(R.layout.category_brief_view, null)

        // set category name in view
        val categoryName = newCategoryView.findViewById<TextView>(R.id.category_name_text_view)
        categoryName.text = category.name

        val categoryIcon = newCategoryView.findViewById<ImageView>(R.id.category_icon_image_view)
        categoryIcon.setImageResource(category.iconID)

        val deleteCategoryImageView = newCategoryView.findViewById<ImageView>(R.id.delete_category_image_view)

        deleteCategoryImageView.setOnClickListener{

            categoryDao.delete(category)

            clearCategoriesFromLayout()
            listCategoriesByType()


        }

        binding.categoryLinearLayoutContainer.addView(newCategoryView)


    }

    private fun listCategoriesByType(){

        val categories: List<Category> = categoryDao.findByCategoryType(getSelectedType())

        for (category in categories){
            addCategoryBriefView(category)
        }


    }

    private fun clearCategoriesFromLayout(){

        val linearLayout = binding.categoryLinearLayoutContainer

        if(linearLayout.childCount > 0)
            linearLayout.removeAllViews()

    }




    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



}