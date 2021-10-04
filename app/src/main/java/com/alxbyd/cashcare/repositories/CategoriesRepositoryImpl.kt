package com.alxbyd.cashcare.repositories

import com.alxbyd.cashcare.utils.dataclasses.Category
import com.alxbyd.cashcare.utils.exceptions.NoSuchCategoryException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object CategoriesRepositoryImpl : CategoriesRepository {

    override suspend fun getAllCategories(): StateFlow<List<Category>> {
        return categoryMutableStateFlow
    }

    private val categoryMutableStateFlow = MutableStateFlow(mutableListOf<Category>())

    init {
        categoryMutableStateFlow.value = createMockCategories()
    }

    private fun createMockCategories(): MutableList<Category> {
        val mockCategoriesNames = listOf(
            "No category", "Pets", "Pharmacy", "Auto", "Clothes", "Gifts", "Rent"
        )
        val mockCategoriesList = mutableListOf<Category>()
        for (id in 0..mockCategoriesNames.lastIndex) {
            mockCategoriesList.add(
                Category(
                    id,
                    mockCategoriesNames[id],
                )
            )
        }
        return mockCategoriesList
    }

    override suspend fun addCategory(category: Category) {
        val newCategory = category.copy(
            id = categoryMutableStateFlow.value.lastOrNull()?.id?.plus(1) ?: 1
        )
        val newCategories = mutableListOf<Category>()

        newCategories.addAll(categoryMutableStateFlow.value)
        newCategories.add(newCategory)
        categoryMutableStateFlow.value = newCategories
    }

    @Throws(NoSuchCategoryException::class)
    override suspend fun updateCategory(category: Category) {
        val indexCategory =
            categoryMutableStateFlow.value.indexOfFirst { it.id == category.id }
        if (indexCategory == -1) throw NoSuchCategoryException()
        val newCategories = mutableListOf<Category>()

        newCategories.addAll(categoryMutableStateFlow.value)
        newCategories[indexCategory] = category
        categoryMutableStateFlow.value = newCategories
    }

    @Throws(NoSuchCategoryException::class)
    override suspend fun deleteCategory(category: Category) {
        val newCategories = mutableListOf<Category>()

        newCategories.addAll(categoryMutableStateFlow.value)
        if (!newCategories.remove(category)) throw NoSuchCategoryException()
        categoryMutableStateFlow.value = newCategories
    }

}