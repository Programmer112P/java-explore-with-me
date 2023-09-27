package ru.practicum.mainservice.compilations.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainservice.compilations.dto.CompilationDto;
import ru.practicum.mainservice.compilations.service.CompilationsService;

import java.util.List;

@RestController
@RequestMapping("/compilations")
@Validated
@Slf4j
public class CompilationsPublicController {

    private final CompilationsService compilationsService;

    @Autowired
    public CompilationsPublicController(CompilationsService compilationsService) {
        this.compilationsService = compilationsService;
    }

    @GetMapping
    public List<CompilationDto> getCompilationsPage(@RequestParam(required = false) Boolean pinned,
                                                    @RequestParam(name = "from", required = false, defaultValue = "0") Long offset,
                                                    @RequestParam(name = "size", required = false, defaultValue = "10") Integer limit) {
        log.info("CompilationsPublicController getCompilationsPage: request to get compilations page");
        List<CompilationDto> response = compilationsService.getCompilationsPage(pinned, offset, limit);
        log.info("CompilationsPublicController getCompilationsPage: completed request to get compilations page");
        return response;
    }

    @GetMapping("/{compId}")
    public CompilationDto getCompilation(@PathVariable(name = "compId") Long id) {
        log.info("CompilationsPublicController getCompilationsPage: request to get compilation with id {}", id);
        CompilationDto response = compilationsService.getCompilation(id);
        log.info("CompilationsPublicController getCompilationsPage: completed request to get compilation with id {}", id);
        return response;

    }
}
