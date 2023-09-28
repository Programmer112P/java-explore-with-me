package ru.practicum.mainservice.categories.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.mainservice.categories.dto.CategoryDto;
import ru.practicum.mainservice.categories.dto.NewCategoryDto;
import ru.practicum.mainservice.categories.mapper.CategoryMapper;
import ru.practicum.mainservice.categories.model.Category;
import ru.practicum.mainservice.categories.repository.CategoryRepository;
import ru.practicum.mainservice.exception.ConflictException;
import ru.practicum.mainservice.exception.NotFoundException;
import ru.practicum.mainservice.pagination.OffsetBasedPageRequest;

import java.util.List;

@Service
public class CategoriesServiceImpl implements CategoriesService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Autowired
    public CategoriesServiceImpl(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryDto> getAllCategories(Long offset, Integer limit) {
        OffsetBasedPageRequest offsetBasedPageRequest = new OffsetBasedPageRequest(offset, limit);
        List<Category> categories = categoryRepository.findAll(offsetBasedPageRequest).toList();
        return categoryMapper.getDtoListFromModelList(categories);
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryDto getCategoryById(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new NotFoundException("Category not found"));
        return categoryMapper.getDtoFromModel(category);
    }

    @Override
    @Transactional
    public CategoryDto createCategory(NewCategoryDto newCategoryDto) {
        try {
            Category categoryToSave = categoryMapper.getModelFromDto(newCategoryDto);
            Category category = categoryRepository.save(categoryToSave);
            return categoryMapper.getDtoFromModel(category);
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException(e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new NotFoundException("Category not found"));
        try {
            categoryRepository.delete(category);
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException(e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public CategoryDto updateCategory(CategoryDto categoryDto) {
        Category category = categoryMapper.getModelFromDto(categoryDto);
        try {
            Category updated = categoryRepository.save(category);
            return categoryMapper.getDtoFromModel(updated);
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException(e.getMessage(), e);
        }
    }
}
