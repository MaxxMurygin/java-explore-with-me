package ru.practicum.ewm.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.dto.category.CategoryDto;
import ru.practicum.ewm.dto.category.CategoryMapper;
import ru.practicum.ewm.dto.category.NewCategoryDto;
import ru.practicum.ewm.exception.AlreadyExistException;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.exception.ValidationException;
import ru.practicum.ewm.model.Category;
import ru.practicum.ewm.repository.CategoryRepository;

import org.springframework.data.domain.Pageable;
import ru.practicum.ewm.repository.EventRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DefaultCategoryService implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;

    @Override
    @Transactional
    public CategoryDto create(NewCategoryDto newCategoryDto) {

        return CategoryMapper.toDto(
                categoryRepository.save(CategoryMapper.fromNewCategoryDto(newCategoryDto)));
    }

    @Override
    @Transactional
    public void remove(Long categoryId) {

        categoryRepository.findById(categoryId)
                        .orElseThrow(() -> new NotFoundException(Category.class,
                                String.format(" with id=%d ", categoryId)));
        if (!eventRepository.findAllByCategoryId(categoryId).isEmpty()) {
            throw new ValidationException(Category.class, "Существуют события, связанные с категорией");
        }
        categoryRepository.deleteById(categoryId);
    }

    @Override
    @Transactional
    public CategoryDto update(Long categoryId, NewCategoryDto newCategoryDto) {

        String name = newCategoryDto.getName();

        Category stored = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException(Category.class,
                        String.format(" with id=%d ", categoryId)));

        Optional<Category> existing = categoryRepository.findByName(name);

        if (existing.isPresent() && !existing.get().getName().equals(name)) {
            throw new AlreadyExistException(Category.class,
                    String.format(" with name = %s ", name));
        }
        stored.setName(name);

        return CategoryMapper.toDto(
                categoryRepository.save(stored));
    }

    @Override
    public List<CategoryDto> findAll(Pageable pageable) {

        return categoryRepository.findAll(pageable)
                .stream()
                .map(CategoryMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDto findById(Long categoryId) {

        return CategoryMapper.toDto(categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException(Category.class,
                        String.format(" with id=%d ", categoryId))));
    }
}