package uz.itcenterbaza.service;

import uz.itcenterbaza.service.dto.RegisteredDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link uz.itcenterbaza.domain.Registered}.
 */
public interface RegisteredService {

    /**
     * Save a registered.
     *
     * @param registeredDTO the entity to save.
     * @return the persisted entity.
     */
    RegisteredDTO save(RegisteredDTO registeredDTO);

    /**
     * Get all the registereds.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<RegisteredDTO> findAll(Pageable pageable);


    /**
     * Get the "id" registered.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<RegisteredDTO> findOne(Long id);

    /**
     * Delete the "id" registered.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
