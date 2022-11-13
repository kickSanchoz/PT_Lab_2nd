package ru.pt_lab_2nd.android.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "product")
data class Product(
    @PrimaryKey
    val id: Int,
    val url: String? = null,
    val name: String,
    val price: Int,
    val count: Int
)
