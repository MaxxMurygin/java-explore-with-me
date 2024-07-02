package ru.practicum.ewm.service;

import com.sun.xml.bind.v2.TODO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.dto.category.CategoryDto;
import ru.practicum.ewm.dto.category.CategoryMapper;
import ru.practicum.ewm.dto.category.NewCategoryDto;
import ru.practicum.ewm.exception.EntityAlreadyExistException;
import ru.practicum.ewm.exception.EntityNotFoundException;
import ru.practicum.ewm.model.Category;
import ru.practicum.ewm.repository.CategoryRepository;

@Service
@RequiredArgsConstructor
public class DefaultCategoryService implements CategoryService {
    private final CategoryRepository categoryRepository;
    @Override
    public CategoryDto create(NewCategoryDto newCategoryDto) {
        String name = newCategoryDto.getName();

        if (categoryRepository.findByName(name).isPresent()) {
            throw new EntityAlreadyExistException(Category.class,
                    String.format(" with name = %s ", name));
        }

        return CategoryMapper.toDto(categoryRepository.save(CategoryMapper.fromNewCategoryDto(newCategoryDto)));
    }

    @Override
    public void remove(Long categoryId) {

// TODO Существуют события, связанные с категорией

        categoryRepository.findById(categoryId).
                orElseThrow(() -> new EntityNotFoundException(Category.class,
                        String.format(" with id=%d ", categoryId)));

        categoryRepository.deleteById(categoryId);
    }

    @Override
    public CategoryDto update(Long categoryId, NewCategoryDto newCategoryDto) {
        String name = newCategoryDto.getName();
        Category updated = categoryRepository.findById(categoryId).
                orElseThrow(() -> new EntityNotFoundException(Category.class,
                        String.format(" with id=%d ", categoryId)));

        if (categoryRepository.findByName(name).isPresent()) {
            throw new EntityAlreadyExistException(Category.class,
                    String.format(" with name = %s ", name));
        }
        updated.setName(name);

        return CategoryMapper.toDto(categoryRepository.save(updated));
    }
}
