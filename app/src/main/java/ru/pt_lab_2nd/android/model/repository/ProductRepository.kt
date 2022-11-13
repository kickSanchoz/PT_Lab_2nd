package ru.pt_lab_2nd.android.model.repository

import androidx.lifecycle.liveData
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import ru.pt_lab_2nd.android.model.Product
import ru.pt_lab_2nd.android.model.datasource.ProductDaoDataSource
import javax.inject.Inject

class ProductRepository @Inject constructor(
    private val productDaoDataSource: ProductDaoDataSource,
) {

    fun getAllProducts() = liveData(IO) {
        emitSource(productDaoDataSource.getAllProducts())
    }

    suspend fun insertAllProducts(productList: List<Product>) = withContext(IO) {
        productDaoDataSource.insertAllProducts(productList)
    }
}
