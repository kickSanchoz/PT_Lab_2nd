package ru.pt_lab_2nd.android.model.repository

import androidx.lifecycle.liveData
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import ru.pt_lab_2nd.android.model.Product
import ru.pt_lab_2nd.android.model.datasource.ProductDaoDataSource
import ru.pt_lab_2nd.android.utils.Resource
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

    suspend fun insertProduct(product: Product) = withContext(IO) {
        productDaoDataSource.insertProduct(product)
    }

    suspend fun updateProduct(product: Product) = withContext(IO) {
        if (product.count > 0) {
            return@withContext productDaoDataSource.updateProduct(
                product.copy(count = product.count - 1)
            )
        } else {
            return@withContext Resource.error("Количество должно быть > 0")
        }
    }
}
