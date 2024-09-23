package org.dreamdevwork.shop.repositories;

import org.dreamdevwork.shop.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Category findByName(String category);
}
