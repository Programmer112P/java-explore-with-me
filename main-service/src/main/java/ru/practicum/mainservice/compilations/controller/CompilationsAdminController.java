package ru.practicum.mainservice.compilations.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainservice.compilations.dto.CompilationDto;
import ru.practicum.mainservice.compilations.dto.NewCompilationDto;
import ru.practicum.mainservice.compilations.dto.UpdateCompilationRequest;
import ru.practicum.mainservice.compilations.service.CompilationsService;

import javax.validation.Valid;

@RestController
@RequestMapping("admin/compilations")
@Slf4j
@Validated
public class CompilationsAdminController {

    private final CompilationsService compilationsService;

    @Autowired
    public CompilationsAdminController(CompilationsService compilationsService) {
        this.compilationsService = compilationsService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto createCompilation(@RequestBody @Valid NewCompilationDto newCompilationDto) {
        log.info("CompilationsAdminController createCompilation: request to create compilation");
        CompilationDto response = compilationsService.createCompilation(newCompilationDto);
        log.info("CompilationsAdminController createCompilation: completed request to create compilation");
        return response;
    }

    @PatchMapping("/{compId}")
    public CompilationDto updateCompilation(@PathVariable(name = "compId") Long id,
                                            @RequestBody @Valid UpdateCompilationRequest updateCompilationRequest) {
        log.info("CompilationsAdminController updateCompilation: request to update compilation");
        CompilationDto response = compilationsService.updateCompilation(id, updateCompilationRequest);
        log.info("CompilationsAdminController updateCompilation: completed request to update compilation");
        return response;
    }

    @DeleteMapping("/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompilation(@PathVariable(name = "compId") Long id) {
        log.info("CompilationsAdminController deleteCompilation: request to delete compilation with id {}", id);
        compilationsService.deleteCompilation(id);
        log.info("CompilationsAdminController deleteCompilation: completed request to delete compilation with id {}", id);
    }

}
