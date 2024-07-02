package ru.practicum.ewm.service;

import ru.practicum.ewm.dto.category.CategoryDto;
import ru.practicum.ewm.dto.category.NewCategoryDto;

public interface CategoryService {
    CategoryDto create(NewCategoryDto newCategoryDto);

    void remove(Long categoryId);

    CategoryDto update(Long categoryId, NewCategoryDto newCategoryDto);

}
