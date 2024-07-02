package ru.practicum.ewm.dto.category;

import ru.practicum.ewm.model.Category;

public class CategoryMapper {
    public static CategoryDto toDto(Category category) {
        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }

    public static Category fromNewCategoryDto(NewCategoryDto newCategoryDto) {
        Category category = new Category();

        category.setName(newCategoryDto.getName());
        return category;
    }
}
