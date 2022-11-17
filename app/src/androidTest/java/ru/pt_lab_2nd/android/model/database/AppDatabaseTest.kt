package ru.pt_lab_2nd.android.model.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import ru.pt_lab_2nd.android.model.Product
import ru.pt_lab_2nd.android.model.dao.ProductDao
import ru.pt_lab_2nd.android.model.datasource.ProductDaoDataSource
import ru.pt_lab_2nd.android.model.repository.ProductRepository
import ru.pt_lab_2nd.android.utils.Resource

@RunWith(AndroidJUnit4::class)
class AppDatabaseTest {
    private lateinit var db: AppDatabase
    private lateinit var productDao: ProductDao
    private lateinit var productDaoDataSource: ProductDaoDataSource
    private lateinit var productRepository: ProductRepository

    private lateinit var productList: List<Product>

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).allowMainThreadQueries()
            .build()
        productDao = db.productDao()
        productDaoDataSource = ProductDaoDataSource(productDao)
        productRepository = ProductRepository(productDaoDataSource)

        productList = listOf(
            Product(
                id = 1,
                name = "Шкаф",
                price = 250,
                count = 32,
            ),
            Product(
                id = 2,
                name = "Тумбочка",
                price = 160,
                count = 634,
            ),
        )
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun testInsertProduct() = runBlocking {
        insertProducts()
    }

    private suspend fun insertProducts() {
        val initialProducts = productDao.getAllProductsAsync()
        println("insertedProducts: $initialProducts")
        assertEquals(emptyList<Product>(), initialProducts)

        productDao.insertAllProducts(productList)

        val insertedProducts = productDao.getAllProductsAsync()
        println("obtainedProducts: $insertedProducts")
        assertEquals(productList, insertedProducts)
    }

    @Test
    fun testUpdateProduct() = runBlocking {
        val product = Product(
            id = 3,
            name = "Кровать",
            price = 210,
            count = 16,
        )

        val res = productRepository.updateProduct(product)
        println("res: $res")
        assertEquals(Resource.Status.SUCCESS, res.status)
    }

    @Test
    fun testDeleteProducts() = runBlocking {
        insertProducts()

        productDao.deleteAllProducts()

        val deletedProducts = productDao.getAllProductsAsync()
        println("deletedProducts: $deletedProducts")
        assertEquals(emptyList<Product>(), deletedProducts)
    }
}