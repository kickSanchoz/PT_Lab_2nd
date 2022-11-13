package ru.pt_lab_2nd.android.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.pt_lab_2nd.android.model.datasource.ProductDaoDataSource
import ru.pt_lab_2nd.android.model.repository.ProductRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideProductRepository(
        productDaoDataSource: ProductDaoDataSource,
    ) =
        ProductRepository(
            productDaoDataSource
        )
}