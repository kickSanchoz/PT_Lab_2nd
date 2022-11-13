package ru.pt_lab_2nd.android.model.database

import android.content.Context
import androidx.room.*
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import ru.pt_lab_2nd.android.model.Product
import ru.pt_lab_2nd.android.model.dao.ProductDao
import ru.pt_lab_2nd.android.utils.DATASTORE_NAME
import java.lang.reflect.ParameterizedType

class ProductConverter {

    private val moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()
    private val listProducts : ParameterizedType = Types.newParameterizedType(List::class.java, Product::class.java)
    private val jsonAdapter: JsonAdapter<List<Product>> = moshi.adapter(listProducts)

    @TypeConverter
    fun listProductToJsonStr(listMyModel: List<Product>?): String? {
        return jsonAdapter.toJson(listMyModel)
    }

    @TypeConverter
    fun jsonStrToListProduct(jsonStr: String?): List<Product>? {
        return jsonStr?.let { jsonAdapter.fromJson(jsonStr) }
    }
}

@Database(
    entities = [
        Product::class,
    ],
    version = 1
)
@TypeConverters(
    ProductConverter::class
)
abstract class AppDatabase: RoomDatabase() {
    abstract fun productDao(): ProductDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase =
            instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also {
                    instance = it
                }
            }

        private fun buildDatabase(appContext: Context) =
            Room.databaseBuilder(appContext, AppDatabase::class.java, DATASTORE_NAME)
                .fallbackToDestructiveMigration()
                .build()
    }
}