package ru.practicum.mainservice.categories.service;

import ru.practicum.mainservice.categories.dto.CategoryDto;
import ru.practicum.mainservice.categories.dto.NewCategoryDto;

import java.util.List;

public interface CategoriesService {
    List<CategoryDto> getAllCategories(Long offset, Integer limit);

    CategoryDto getCategoryById(Long id);

    CategoryDto createCategory(NewCategoryDto newCategoryDto);

    void deleteCategory(Long id);

    CategoryDto updateCategory(CategoryDto categoryDto);
}
