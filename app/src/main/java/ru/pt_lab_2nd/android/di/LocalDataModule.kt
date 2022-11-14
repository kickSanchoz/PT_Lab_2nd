package ru.pt_lab_2nd.android.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.pt_lab_2nd.android.model.dao.ProductDao
import ru.pt_lab_2nd.android.model.dao.PurchaseDao
import ru.pt_lab_2nd.android.model.database.AppDatabase
import ru.pt_lab_2nd.android.model.datasource.ProductDaoDataSource
import ru.pt_lab_2nd.android.model.datasource.PurchaseDaoDataSource
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalDataModule {

    @Provides
    fun provideContext(@ApplicationContext context: Context) = context

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext appContext: Context) =
        AppDatabase.getDatabase(appContext)

    //------------------------Dao------------------------
    @Singleton
    @Provides
    fun provideProductDao(db: AppDatabase) = db.productDao()

    @Singleton
    @Provides
    fun providePurchaseDao(db: AppDatabase) = db.purchaseDao()

    //------------------------DataSources------------------------
    @Singleton
    @Provides
    fun provideProductDaoDataSource(productDao: ProductDao) = ProductDaoDataSource(productDao)

    @Singleton
    @Provides
    fun providePurchaseDaoDataSource(purchaseDao: PurchaseDao) = PurchaseDaoDataSource(purchaseDao)
}