package ru.pt_lab_2nd.android.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "product")
data class Product(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val url: String? = null,
    val name: String,
    val price: Int,
    val count: Int,
) : Parcelable
