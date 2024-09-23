package org.dreamdevwork.shop.services.impl;

import lombok.RequiredArgsConstructor;
import org.dreamdevwork.shop.exceptions.AlreadyExistsException;
import org.dreamdevwork.shop.exceptions.ResourceNotFoundException;
import org.dreamdevwork.shop.models.Category;
import org.dreamdevwork.shop.repositories.CategoryRepository;
import org.dreamdevwork.shop.services.ICategoryService;
import org.springframework.stereotype.Service;

import java.nio.channels.AlreadyConnectedException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService {
    private final CategoryRepository categoryRepository;

    @Override
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
    }

    @Override
    public Category getCategoryByName(String name) {
        return categoryRepository.findByName(name);
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Category addCategory(Category category) {
        return Optional.of(category)
                .filter(c -> !categoryRepository.existsByName(c.getName()))
                .map(categoryRepository :: save)
                .orElseThrow(()-> new AlreadyExistsException(category.getName() + "already exist"));
    }

    @Override
    public Category updateCategory(Category category, Long id) {
        return Optional.ofNullable(getCategoryById(id)).map( oldCategorie -> {
            oldCategorie.setName(category.getName());
            return categoryRepository.save(oldCategorie);
        }).orElseThrow(
                () -> new ResourceNotFoundException("Category not found")
        );
    }

    @Override
    public void deleteCategoryById(Long id) {
        categoryRepository.findById(id)
                .ifPresentOrElse(categoryRepository ::delete,
                        () -> new ResourceNotFoundException("Category not found"));
    }
}
