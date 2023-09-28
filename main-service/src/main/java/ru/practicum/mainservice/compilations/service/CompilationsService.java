package ru.practicum.mainservice.compilations.service;

import ru.practicum.mainservice.compilations.dto.CompilationDto;
import ru.practicum.mainservice.compilations.dto.NewCompilationDto;
import ru.practicum.mainservice.compilations.dto.UpdateCompilationRequest;

import java.util.List;

public interface CompilationsService {
    CompilationDto createCompilation(NewCompilationDto newCompilationDto);

    CompilationDto updateCompilation(Long id, UpdateCompilationRequest updateCompilationRequest);

    void deleteCompilation(Long id);

    List<CompilationDto> getCompilationsPage(Boolean pinned, Long offset, Integer limit);

    CompilationDto getCompilation(Long id);
}
