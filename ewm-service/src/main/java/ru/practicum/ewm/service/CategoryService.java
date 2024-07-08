package ru.practicum.ewm.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.ewm.dto.category.CategoryDto;
import ru.practicum.ewm.dto.category.NewCategoryDto;

import java.util.List;

public interface CategoryService {
    CategoryDto create(NewCategoryDto newCategoryDto);

    void remove(Long categoryId);

    CategoryDto update(Long categoryId, NewCategoryDto newCategoryDto);

    List<CategoryDto> findAll(Pageable pageable);

    CategoryDto findById(Long categoryId);

}
