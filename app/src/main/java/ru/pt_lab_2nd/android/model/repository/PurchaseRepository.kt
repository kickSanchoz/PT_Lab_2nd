package ru.pt_lab_2nd.android.model.repository

import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import ru.pt_lab_2nd.android.model.Purchase
import ru.pt_lab_2nd.android.model.datasource.PurchaseDaoDataSource
import ru.pt_lab_2nd.android.utils.Resource
import javax.inject.Inject

class PurchaseRepository @Inject constructor(
    private val purchaseDaoDataSource: PurchaseDaoDataSource,
    private val productRepository: ProductRepository,
) {

    suspend fun insertPurchase(purchase: Purchase) = withContext(IO) {
        val res = purchaseDaoDataSource.insertPurchase(purchase)

        if (res.status == Resource.Status.SUCCESS) {
            return@withContext productRepository.updateProduct(purchase.product!!)
        }
        else {
            return@withContext res
        }
    }
}