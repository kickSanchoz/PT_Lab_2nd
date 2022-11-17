package ru.pt_lab_2nd

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import ru.pt_lab_2nd.android.model.Product
import ru.pt_lab_2nd.android.model.dao.ProductDao
import ru.pt_lab_2nd.android.model.database.AppDatabase
import ru.pt_lab_2nd.android.model.datasource.ProductDaoDataSource
import ru.pt_lab_2nd.android.model.repository.ProductRepository

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }
}

@RunWith(AndroidJUnit4::class)
class ProductRepositoryTest {
    private lateinit var context: Context
    private lateinit var appDatabase: AppDatabase
    private lateinit var productDao: ProductDao
    private lateinit var productDaoDataSource: ProductDaoDataSource
    private lateinit var productRepository: ProductRepository

    @Before
    fun init() {
        context = ApplicationProvider.getApplicationContext()
        appDatabase = AppDatabase.getDatabase(context)
        productDao = appDatabase.productDao()
        productDaoDataSource = ProductDaoDataSource(productDao)
        productRepository = ProductRepository(productDaoDataSource)
    }

    @Test
    fun mda() {
        val productList = mutableListOf(
            Product(
                id = 1,
                url = "https://www.pngrepo.com/png/21590/180/chair.png",
                name = "Стул",
                price = 100,
                count = 10,
            ),
            Product(
                id = 2,
                url = "https://www.pngrepo.com/png/164217/180/table.png",
                name = "Стол",
                price = 150,
                count = 3,
            ),
            Product(
                id = 3,
                url = "https://www.pngrepo.com/png/8313/180/couch.png",
                name = "Диван",
                price = 200,
                count = 1,
            ),
        )

        print("Result: ${productRepository.getAllProducts()}")
    }
}