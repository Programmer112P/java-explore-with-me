package ru.practicum.mainservice.categories.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.mainservice.categories.dto.CategoryDto;
import ru.practicum.mainservice.categories.dto.NewCategoryDto;
import ru.practicum.mainservice.categories.model.Category;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CategoryMapper {

    public Category getModelFromDto(NewCategoryDto newCategoryDto) {
        return Category.builder()
                .name(newCategoryDto.getName())
                .build();
    }

    public Category getModelFromDto(CategoryDto categoryDto) {
        return Category.builder()
                .id(categoryDto.getId())
                .name(categoryDto.getName())
                .build();
    }

    public CategoryDto getDtoFromModel(Category category) {
        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }

    public List<CategoryDto> getDtoListFromModelList(List<Category> categories) {
        return categories.stream().map(this::getDtoFromModel).collect(Collectors.toList());
    }
}
