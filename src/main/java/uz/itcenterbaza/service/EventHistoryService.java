package uz.itcenterbaza.service;

import uz.itcenterbaza.service.dto.EventHistoryDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link uz.itcenterbaza.domain.EventHistory}.
 */
public interface EventHistoryService {

    /**
     * Save a eventHistory.
     *
     * @param eventHistoryDTO the entity to save.
     * @return the persisted entity.
     */
    EventHistoryDTO save(EventHistoryDTO eventHistoryDTO);

    /**
     * Get all the eventHistories.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<EventHistoryDTO> findAll(Pageable pageable);

    /**
     * Get all the eventHistories with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    Page<EventHistoryDTO> findAllWithEagerRelationships(Pageable pageable);


    /**
     * Get the "id" eventHistory.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<EventHistoryDTO> findOne(Long id);

    /**
     * Delete the "id" eventHistory.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
