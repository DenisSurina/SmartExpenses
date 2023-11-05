package faks.android.smartexpenses.view

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.room.Room
import faks.android.smartexpenses.R
import faks.android.smartexpenses.databinding.FragmentCategoriesBinding
import faks.android.smartexpenses.model.Category
import faks.android.smartexpenses.model.SmartExpensesLocalDatabase

class CategoriesFragment : Fragment() {


    private var _binding: FragmentCategoriesBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentCategoriesBinding.inflate(inflater, container, false)


        val mainFloatingButton = binding.categoryFragmentMainFloatingButton

        val db = Room.databaseBuilder(
            requireContext(),
            SmartExpensesLocalDatabase::class.java, "main-db"
        ).allowMainThreadQueries().build()

        val categoryDao = db.categoryDao()
        val categories: List<Category> = categoryDao.getAll()

        for (category in categories){
            addCategoryBriefView(category)
        }


        mainFloatingButton.setOnClickListener {
            activity?.let {
                val builder = AlertDialog.Builder(it)
                val customLayout = inflater.inflate(R.layout.add_category_popup_window_layout, null)
                val categoryName = customLayout.findViewById<EditText>(R.id.add_category_name_edit_text)
                val categoryType = customLayout.findViewById<EditText>(R.id.add_category_type_edit_text)

                builder.setView(customLayout)
                    // Add action buttons
                    .setPositiveButton("Add") { dialog, id ->

                        val name = categoryName.text.toString()
                        val type = categoryType.text.toString()

                        val categoryLength =  categoryDao.getAll().size + 1

                        val newCategory = Category( categoryLength,name,type,"icon")

                        categoryDao.insertAll(newCategory)
                        addCategoryBriefView(newCategory)

                    }
                    .setNegativeButton("Exit",
                        DialogInterface.OnClickListener { dialog, id ->



                        })

                val dialog = builder.create()
                dialog.show()
            }
        }



        return binding.root

    }

    private fun addCategoryBriefView(category: Category){

        val newCategoryView = requireActivity().layoutInflater.inflate(R.layout.category_brief_view, null)

        val categoryName = newCategoryView.findViewById<TextView>(R.id.category_name_text_view)
        val categoryType = newCategoryView.findViewById<TextView>(R.id.category_type_text_view)


        categoryName.text = category.name
        categoryType.text = category.type

        binding.categoryLinearLayoutContainer.addView(newCategoryView)


    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}