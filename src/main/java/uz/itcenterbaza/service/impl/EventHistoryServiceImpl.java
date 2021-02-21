package uz.itcenterbaza.service.impl;

import uz.itcenterbaza.service.EventHistoryService;
import uz.itcenterbaza.domain.EventHistory;
import uz.itcenterbaza.repository.EventHistoryRepository;
import uz.itcenterbaza.service.dto.EventHistoryDTO;
import uz.itcenterbaza.service.mapper.EventHistoryMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link EventHistory}.
 */
@Service
@Transactional
public class EventHistoryServiceImpl implements EventHistoryService {

    private final Logger log = LoggerFactory.getLogger(EventHistoryServiceImpl.class);

    private final EventHistoryRepository eventHistoryRepository;

    private final EventHistoryMapper eventHistoryMapper;

    public EventHistoryServiceImpl(EventHistoryRepository eventHistoryRepository, EventHistoryMapper eventHistoryMapper) {
        this.eventHistoryRepository = eventHistoryRepository;
        this.eventHistoryMapper = eventHistoryMapper;
    }

    @Override
    public EventHistoryDTO save(EventHistoryDTO eventHistoryDTO) {
        log.debug("Request to save EventHistory : {}", eventHistoryDTO);
        EventHistory eventHistory = eventHistoryMapper.toEntity(eventHistoryDTO);
        eventHistory = eventHistoryRepository.save(eventHistory);
        return eventHistoryMapper.toDto(eventHistory);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EventHistoryDTO> findAll(Pageable pageable) {
        log.debug("Request to get all EventHistories");
        return eventHistoryRepository.findAll(pageable)
            .map(eventHistoryMapper::toDto);
    }


    public Page<EventHistoryDTO> findAllWithEagerRelationships(Pageable pageable) {
        return eventHistoryRepository.findAllWithEagerRelationships(pageable).map(eventHistoryMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EventHistoryDTO> findOne(Long id) {
        log.debug("Request to get EventHistory : {}", id);
        return eventHistoryRepository.findOneWithEagerRelationships(id)
            .map(eventHistoryMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete EventHistory : {}", id);
        eventHistoryRepository.deleteById(id);
    }
}
