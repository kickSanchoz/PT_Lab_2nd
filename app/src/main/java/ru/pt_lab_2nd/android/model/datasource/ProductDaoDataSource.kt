package ru.pt_lab_2nd.android.model.datasource

import ru.pt_lab_2nd.android.base.BaseDaoDataSource
import ru.pt_lab_2nd.android.model.Product
import ru.pt_lab_2nd.android.model.dao.ProductDao
import javax.inject.Inject

class ProductDaoDataSource @Inject constructor(
    private val productDao: ProductDao,
) : BaseDaoDataSource() {

    fun getAllProducts() = getResult {
        productDao.getAllProducts()
    }

    suspend fun insertAllProducts(productList: List<Product>) = getResultAsync {
        productDao.insertAllProducts(productList)
    }

    suspend fun insertProduct(product: Product) = getResultAsync {
        productDao.insertProduct(product)
    }

    suspend fun updateProduct(product: Product) = getResultAsync {
        productDao.updateProduct(product.id, product.count)
    }
}