package ru.tracefamily.shoesshop.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.tracefamily.shoesshop.domain.repo.ApiInfoServiceRepo
import ru.tracefamily.shoesshop.repo.ApiInfoServiceRepoImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepoModule {

    @Provides
    @Singleton
    fun provideApiInfoServiceRepo() : ApiInfoServiceRepo = ApiInfoServiceRepoImpl()

}