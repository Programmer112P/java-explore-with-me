package ru.practicum.mainservice.compilations.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.mainservice.compilations.dto.CompilationDto;
import ru.practicum.mainservice.compilations.dto.NewCompilationDto;
import ru.practicum.mainservice.compilations.model.Compilation;
import ru.practicum.mainservice.events.dto.EventShortDto;
import ru.practicum.mainservice.events.mapper.EventMapper;
import ru.practicum.mainservice.events.model.Event;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CompilationMapper {

    private final EventMapper eventMapper;

    @Autowired
    public CompilationMapper(EventMapper eventMapper) {
        this.eventMapper = eventMapper;
    }

    public Compilation getModelFromNewCompilationDto(NewCompilationDto newCompilationDto, List<Event> events) {
        return Compilation.builder()
                .pinned(newCompilationDto.getPinned())
                .title(newCompilationDto.getTitle())
                .events(events)
                .build();
    }

    public CompilationDto getDtoFromModel(Compilation compilation) {
        List<Event> events = compilation.getEvents();
        List<EventShortDto> eventShortDtoList = eventMapper.getEventShortListDtoFromModelList(events);
        return CompilationDto.builder()
                .id(compilation.getId())
                .title(compilation.getTitle())
                .events(eventShortDtoList)
                .pinned(compilation.getPinned())
                .build();
    }

    public List<CompilationDto> getDtoListFromModelList(List<Compilation> compilations) {
        return compilations.stream().map(this::getDtoFromModel).collect(Collectors.toList());
    }
}
