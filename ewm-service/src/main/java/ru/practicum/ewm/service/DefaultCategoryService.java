package ru.practicum.ewm.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.dto.category.CategoryDto;
import ru.practicum.ewm.dto.category.CategoryMapper;
import ru.practicum.ewm.dto.category.NewCategoryDto;
import ru.practicum.ewm.exception.AlreadyExistException;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.model.Category;
import ru.practicum.ewm.repository.CategoryRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DefaultCategoryService implements CategoryService {
    private final CategoryRepository categoryRepository;
    @Override
    public CategoryDto create(NewCategoryDto newCategoryDto) {
        String name = newCategoryDto.getName();

        if (categoryRepository.findByName(name).isPresent()) {
            throw new AlreadyExistException(Category.class,
                    String.format(" with name = %s ", name));
        }

        return CategoryMapper.toDto(categoryRepository.save(CategoryMapper.fromNewCategoryDto(newCategoryDto)));
    }

    @Override
    public void remove(Long categoryId) {

// TODO Существуют события, связанные с категорией

        categoryRepository.findById(categoryId).
                orElseThrow(() -> new NotFoundException(Category.class,
                        String.format(" with id=%d ", categoryId)));

        categoryRepository.deleteById(categoryId);
    }

    @Override
    public CategoryDto update(Long categoryId, NewCategoryDto newCategoryDto) {
        String name = newCategoryDto.getName();

        Category stored = categoryRepository.findById(categoryId).
                orElseThrow(() -> new NotFoundException(Category.class,
                        String.format(" with id=%d ", categoryId)));

        Optional<Category> existing = categoryRepository.findByName(name);

        if (existing.isPresent() && !existing.get().getName().equals(name)) {
            throw new AlreadyExistException(Category.class,
                    String.format(" with name = %s ", name));
        }
        stored.setName(name);

        return CategoryMapper.toDto(categoryRepository.save(stored));
    }
}
