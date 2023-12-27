package faks.android.smartexpenses.model

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query


@Dao
interface CategoryDao {

    @Query("SELECT * FROM category")
    fun getAll(): LiveData<List<Category>>

    @Query("SELECT * FROM category WHERE name LIKE :categoryID LIMIT 1")
    fun getByCategoryName(categoryID: String): Category

    @Query("SELECT * FROM category WHERE type LIKE :categoryType")
    fun findByCategoryType(categoryType: String): List<Category>

    @Insert
    fun insertAll(vararg categories: Category)

    @Delete
    fun delete(category: Category)


}