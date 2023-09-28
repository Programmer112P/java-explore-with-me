package ru.practicum.mainservice.categories.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainservice.categories.dto.CategoryDto;
import ru.practicum.mainservice.categories.service.CategoriesService;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/categories")
@Validated
public class CategoriesPublicController {

    private final CategoriesService categoriesService;

    @Autowired
    public CategoriesPublicController(CategoriesService categoriesService) {
        this.categoriesService = categoriesService;
    }

    @GetMapping
    public List<CategoryDto> getAllCategories(@RequestParam(name = "from", required = false, defaultValue = "0") Long offset,
                                              @RequestParam(name = "size", required = false, defaultValue = "10") Integer limit) {
        log.info("CategoriesController getCategories: request to get all categories");
        List<CategoryDto> response = categoriesService.getAllCategories(offset, limit);
        log.info("CategoriesController getCategories: completed request to get all categories");
        return response;
    }

    @GetMapping("/{compId}")
    public CategoryDto getCategoryById(@PathVariable(name = "compId") final Long id) {
        log.info("CategoriesController getCategoryById: request to get category id {}", id);
        CategoryDto response = categoriesService.getCategoryById(id);
        log.info("CategoriesController getCategoryById: completed request to get category id {}", id);
        return response;
    }
}
