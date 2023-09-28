package ru.practicum.mainservice.compilations.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.mainservice.compilations.dto.CompilationDto;
import ru.practicum.mainservice.compilations.dto.NewCompilationDto;
import ru.practicum.mainservice.compilations.dto.UpdateCompilationRequest;
import ru.practicum.mainservice.compilations.mapper.CompilationMapper;
import ru.practicum.mainservice.compilations.model.Compilation;
import ru.practicum.mainservice.compilations.repository.CompilationRepository;
import ru.practicum.mainservice.events.model.Event;
import ru.practicum.mainservice.events.repository.EventRepository;
import ru.practicum.mainservice.exception.ConflictException;
import ru.practicum.mainservice.exception.NotFoundException;
import ru.practicum.mainservice.pagination.OffsetBasedPageRequest;

import java.util.List;

@Service
public class CompilationsServiceImpl implements CompilationsService {

    private final CompilationMapper compilationMapper;
    private final EventRepository eventRepository;
    private final CompilationRepository compilationRepository;

    @Autowired
    public CompilationsServiceImpl(CompilationMapper compilationMapper, EventRepository eventRepository,
                                   CompilationRepository compilationRepository) {
        this.compilationMapper = compilationMapper;
        this.eventRepository = eventRepository;
        this.compilationRepository = compilationRepository;
    }

    @Override
    @Transactional
    public CompilationDto createCompilation(NewCompilationDto newCompilationDto) {
        List<Event> events = eventRepository.findAllById(newCompilationDto.getEvents());
        if (events.size() != newCompilationDto.getEvents().size()) {
            throw new ConflictException("Event not found in compilation");
        }
        Compilation compilation = compilationMapper.getModelFromNewCompilationDto(newCompilationDto, events);
        Compilation created = compilationRepository.save(compilation);
        return compilationMapper.getDtoFromModel(created);
    }

    @Override
    @Transactional
    public CompilationDto updateCompilation(Long id, UpdateCompilationRequest updateCompilationRequest) {
        Compilation compilation = compilationRepository.findById(id).orElseThrow(() -> new NotFoundException("Compilation not found"));
        updateCompilationFields(compilation, updateCompilationRequest);
        Compilation updated = compilationRepository.save(compilation);
        return compilationMapper.getDtoFromModel(updated);
    }

    @Override
    @Transactional
    public void deleteCompilation(Long id) {
        Compilation compilation = compilationRepository.findById(id).orElseThrow(() -> new NotFoundException("Event not found"));
        compilationRepository.delete(compilation);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CompilationDto> getCompilationsPage(Boolean pinned, Long offset, Integer limit) {
        OffsetBasedPageRequest offsetBasedPageRequest = new OffsetBasedPageRequest(offset, limit);
        List<Compilation> compilations;
        if (pinned == null) {
            compilations = compilationRepository.findAll(offsetBasedPageRequest).getContent();
        } else {
            compilations = compilationRepository.findAllByPinned(pinned, offsetBasedPageRequest);
        }
        return compilationMapper.getDtoListFromModelList(compilations);
    }

    @Override
    @Transactional(readOnly = true)
    public CompilationDto getCompilation(Long id) {
        Compilation compilation = compilationRepository.findById(id).orElseThrow(() -> new NotFoundException("Compilation not found"));
        return compilationMapper.getDtoFromModel(compilation);
    }

    private void updateCompilationFields(Compilation compilation, UpdateCompilationRequest updateCompilationRequest) {
        if (updateCompilationRequest.getPinned() != null) {
            compilation.setPinned(updateCompilationRequest.getPinned());
        }
        if (updateCompilationRequest.getTitle() != null) {
            compilation.setTitle(updateCompilationRequest.getTitle());
        }
        if (updateCompilationRequest.getEvents() != null) {
            List<Event> events = eventRepository.findAllById(updateCompilationRequest.getEvents());
            compilation.setEvents(events);
        }
    }
}
