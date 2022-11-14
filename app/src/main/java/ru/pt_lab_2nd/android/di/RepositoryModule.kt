package ru.pt_lab_2nd.android.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.pt_lab_2nd.android.model.datasource.ProductDaoDataSource
import ru.pt_lab_2nd.android.model.datasource.PurchaseDaoDataSource
import ru.pt_lab_2nd.android.model.repository.ProductRepository
import ru.pt_lab_2nd.android.model.repository.PurchaseRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideProductRepository(
        productDaoDataSource: ProductDaoDataSource,
    ) = ProductRepository(
        productDaoDataSource
    )

    @Singleton
    @Provides
    fun providePurchaseRepository(
        purchaseDaoDataSource: PurchaseDaoDataSource,
        productRepository: ProductRepository,
    ) = PurchaseRepository(
        purchaseDaoDataSource,
        productRepository,
    )
}