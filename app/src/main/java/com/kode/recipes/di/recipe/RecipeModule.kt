package com.kode.recipes.di.recipe

import com.kode.recipes.data.recipe.network.RecipeApi
import com.kode.recipes.data.recipe.network.RecipeApiImpl
import com.kode.recipes.data.recipe.repository.ImageRepositoryImpl
import com.kode.recipes.data.recipe.repository.RecipeRepositoryImpl
import com.kode.recipes.domain.recipe.repository.ImageRepository
import com.kode.recipes.domain.recipe.repository.RecipeRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RecipeModule {

    @Provides
    @Singleton
    fun provideRecipeApi(api: RecipeApiImpl): RecipeApi = api

    @Provides
    @Singleton
    fun provideRecipeRepository(repository: RecipeRepositoryImpl): RecipeRepository = repository

    @Provides
    @Singleton
    fun provideImageRepository(repository: ImageRepositoryImpl): ImageRepository = repository
}