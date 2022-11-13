package ru.pt_lab_2nd.android.model.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import ru.pt_lab_2nd.android.model.Product

@Dao
interface ProductDao {

    @Query("SELECT * FROM product")
    fun getAllProducts(): LiveData<List<Product>?>

    @Insert(onConflict = REPLACE)
    fun insertAllProducts(productList: List<Product>)

    @Insert(onConflict = REPLACE)
    fun insertProduct(product: Product)


}