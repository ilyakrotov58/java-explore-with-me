package ru.practicum.service.admin.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.dto.CompilationDto;
import ru.practicum.dto.NewCompilationDto;
import ru.practicum.dto.mappers.CompilationDtoMapper;
import ru.practicum.dto.requests.UpdateCompilationRequest;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.model.Event;
import ru.practicum.repository.admin.ICompilationAdminRepository;
import ru.practicum.repository.admin.IEventAdminRepository;
import ru.practicum.service.admin.interfaces.ICompilationAdminService;

import java.util.ArrayList;

@Slf4j
@Service
public class CompilationAdminService implements ICompilationAdminService {

    private final ICompilationAdminRepository compilationAdminRepository;
    private final IEventAdminRepository eventAdminRepository;

    @Autowired
    public CompilationAdminService(
            ICompilationAdminRepository compilationAdminRepository,
            IEventAdminRepository eventAdminRepository) {
        this.compilationAdminRepository = compilationAdminRepository;
        this.eventAdminRepository = eventAdminRepository;
    }

    @Override
    public CompilationDto createCompilation(NewCompilationDto newCompilationDto) {

        var savedCompilation = compilationAdminRepository
                .save(CompilationDtoMapper.fromNewToCompilation(newCompilationDto));

        var eventList = new ArrayList<Event>();

        if (newCompilationDto.getEvents() != null) {
            for (Long eventId : newCompilationDto.getEvents()) {
                var event = eventAdminRepository.findById(eventId);

                if (event.isEmpty()) {
                    throw new NotFoundException(String.format("Event with id = %s is not found", eventId));
                }

                var eventInDb = eventAdminRepository.save(event.get());

                eventList.add(eventInDb);
            }
        }

        savedCompilation.setEventsList(eventList);

        return CompilationDtoMapper.toCompilationDto(savedCompilation);
    }

    @Override
    public void deleteCompilation(long compId) {
        compilationAdminRepository.deleteById(compId);
    }

    @Override
    public CompilationDto updateCompilation(long compId, UpdateCompilationRequest updateCompilationRequest) {
        var compilation = compilationAdminRepository.findById(compId);

        if (compilation.isEmpty()) {
            throw new NotFoundException(String.format("Compilation with id = %s is not found", compId));
        }

        var events = new ArrayList<Event>();

        if (!updateCompilationRequest.getEvents().isEmpty()) {
            for (Long eventId : updateCompilationRequest.getEvents()) {
                var event = eventAdminRepository.findById(eventId);

                if (event.isEmpty()) {
                    throw new NotFoundException(String.format("Event with id = %s is not found", eventId));
                }

                eventAdminRepository.save(event.get());

                events.add(event.get());
            }
        }

        if (updateCompilationRequest.getPinned() != null) {
            compilation.get().setPinned(updateCompilationRequest.getPinned());
        }

        if (updateCompilationRequest.getTitle() != null) {
            compilation.get().setTitle(updateCompilationRequest.getTitle());
        }

        var savedCompilation = compilationAdminRepository.save(compilation.get());

        savedCompilation.setEventsList(events);

        return CompilationDtoMapper.toCompilationDto(savedCompilation);
    }
}
