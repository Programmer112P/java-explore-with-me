package ru.practicum.mainservice.categories.controller;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainservice.categories.dto.CategoryDto;
import ru.practicum.mainservice.categories.dto.NewCategoryDto;
import ru.practicum.mainservice.categories.service.CategoriesService;
import ru.practicum.mainservice.exception.ConflictException;

import javax.validation.Valid;

@RestController
@Slf4j
@RequestMapping("/admin/categories")
@Validated
public class CategoriesAdminController {

    private final CategoriesService categoriesService;

    @Autowired
    public CategoriesAdminController(CategoriesService categoriesService) {
        this.categoriesService = categoriesService;
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto createCategory(@RequestBody @Valid NewCategoryDto newCategoryDto) {
        log.info("CategoryAdminController createCategory: request to create category {}", newCategoryDto);
        try {
            CategoryDto response = categoriesService.createCategory(newCategoryDto);
            log.info("CategoryAdminController createCategory: completed request to create category {}", newCategoryDto);
            return response;
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException("Category with this name already exist");
        }

    }

    @DeleteMapping("/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable(name = "catId") Long id) {
        log.info("CategoryAdminController deleteCategory: request to delete category {}", id);
        try {
            categoriesService.deleteCategory(id);
            log.info("CategoryAdminController deleteCategory: completed request to delete category {}", id);
        } catch (ConstraintViolationException | DataIntegrityViolationException e) {
            throw new ConflictException("");
        }
    }

    @PatchMapping("/{catId}")
    public CategoryDto updateCategory(@PathVariable(name = "catId") Long id,
                                      @RequestBody @Valid CategoryDto categoryDto) {
        log.info("CategoryAdminController updateCategory: request to update category {}", id);
        categoryDto.setId(id); // не понял зачем нужно отдельно это поле отдавать, если оно и так есть в DTO
        try {
            CategoryDto response = categoriesService.updateCategory(categoryDto);
            log.info("CategoryAdminController updateCategory: completed request to update category {}", id);
            return response;
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException("Category with this name already exist");
        }

    }
}
