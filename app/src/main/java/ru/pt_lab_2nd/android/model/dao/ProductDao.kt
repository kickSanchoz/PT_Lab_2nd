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

    //Метод нужен для тестов, т.к. livedata возращает асинхронно
    @Query("SELECT * FROM product")
    suspend fun getAllProductsAsync(): List<Product>?

    @Insert(onConflict = REPLACE)
    fun insertAllProducts(productList: List<Product>)

    @Insert(onConflict = REPLACE)
    fun insertProduct(product: Product)

    @Query("UPDATE product SET count= :count WHERE id LIKE :id")
    fun updateProduct(id: Int, count: Int)

    @Query("DELETE FROM product")
    suspend fun deleteAllProducts()
}