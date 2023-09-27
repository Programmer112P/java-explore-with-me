package ru.practicum.mainservice.categories.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.mainservice.categories.model.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}
