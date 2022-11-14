package ru.pt_lab_2nd.android.model.datasource

import ru.pt_lab_2nd.android.base.BaseDaoDataSource
import ru.pt_lab_2nd.android.model.Purchase
import ru.pt_lab_2nd.android.model.dao.PurchaseDao
import javax.inject.Inject

class PurchaseDaoDataSource @Inject constructor(
    private val purchaseDao: PurchaseDao,
) : BaseDaoDataSource() {

    suspend fun insertPurchase(purchase: Purchase) = getResultAsync {
        purchaseDao.insertPurchase(purchase)
    }
}