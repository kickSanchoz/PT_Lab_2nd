package ru.pt_lab_2nd.android.model.dao

import androidx.room.Dao
import androidx.room.Insert
import ru.pt_lab_2nd.android.model.Purchase

@Dao
interface PurchaseDao {
    @Insert
    suspend fun insertPurchase(purchase: Purchase)
}