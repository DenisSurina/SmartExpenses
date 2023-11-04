package faks.android.smartexpenses.model

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query


@Dao
interface CategoryDao {

    @Query("SELECT * FROM category")
    fun getAll(): List<Category>

    @Query("SELECT * FROM category WHERE category_id LIKE :categoryID LIMIT 1")
    fun findByName(categoryID: String): Category

    @Insert
    fun insertAll(vararg users: Category)

    @Delete
    fun delete(user: Category)


}