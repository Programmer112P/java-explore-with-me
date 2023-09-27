package ru.practicum.mainservice.compilations.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.mainservice.compilations.model.Compilation;
import ru.practicum.mainservice.pagination.OffsetBasedPageRequest;

import java.util.List;

public interface CompilationRepository extends JpaRepository<Compilation, Long> {

    List<Compilation> findAllByPinned(Boolean pinned, OffsetBasedPageRequest pageRequest);
}