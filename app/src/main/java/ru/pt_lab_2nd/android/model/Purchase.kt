package ru.pt_lab_2nd.android.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "purchase")
data class Purchase(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @Embedded(prefix = "product") val product: Product,
    @Embedded(prefix = "customer") val customer: Customer = Customer(),
) {
    fun isValid(): Boolean {
        return customer.isValid()
    }
}
