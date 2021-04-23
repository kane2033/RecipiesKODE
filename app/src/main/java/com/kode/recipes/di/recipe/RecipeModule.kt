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
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object RecipeModule {

    @Provides
    @ViewModelScoped
    fun provideRecipeApi(api: RecipeApiImpl): RecipeApi = api

    @Provides
    @ViewModelScoped
    fun provideRecipeRepository(repository: RecipeRepositoryImpl): RecipeRepository = repository

    @Provides
    @ViewModelScoped
    fun provideImageRepository(repository: ImageRepositoryImpl): ImageRepository = repository
}