package com.alxbyd.cashcare.repositories

import com.alxbyd.cashcare.utils.dataclasses.Category
import kotlinx.coroutines.flow.StateFlow

interface CategoriesRepository {

    suspend fun getAllCategories(): StateFlow<List<Category>>
    suspend fun addCategory(category: Category)
    suspend fun updateCategory(category: Category)
    suspend fun deleteCategory(category: Category)

}