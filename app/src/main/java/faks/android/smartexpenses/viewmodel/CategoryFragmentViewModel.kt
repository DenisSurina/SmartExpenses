package faks.android.smartexpenses.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import faks.android.smartexpenses.model.Category
import faks.android.smartexpenses.model.SmartExpensesLocalDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class CategoryFragmentViewModel(application: Application) : AndroidViewModel(application) {
    private val db: SmartExpensesLocalDatabase = SmartExpensesLocalDatabase.getDatabase(application)

    fun insertCategory(category: Category) {
        viewModelScope.launch(Dispatchers.IO) {
            db.categoryDao().insertAll(category)
        }
    }

    fun deleteCategory(category: Category){
        viewModelScope.launch(Dispatchers.IO){
            db.categoryDao().delete(category)
        }
    }

    val getCategories: List<Category> = db.categoryDao().getAll()


    fun getCategoryByName(categoryName: String, callback: (Category?) -> Unit) {
        viewModelScope.launch {
            val category = withContext(Dispatchers.IO) {
                db.categoryDao().getByCategoryName(categoryName)
            }
            callback(category)
        }
    }

    fun getCategoriesByType(categoryType: String, callback: (List<Category>) -> Unit) {
        viewModelScope.launch {
            val categories = withContext(Dispatchers.IO) {
                db.categoryDao().findByCategoryType(categoryType)
            }
            callback(categories)
        }
    }

}
