package uz.itcenterbaza.service;

import uz.itcenterbaza.service.dto.RegionsDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link uz.itcenterbaza.domain.Regions}.
 */
public interface RegionsService {

    /**
     * Save a regions.
     *
     * @param regionsDTO the entity to save.
     * @return the persisted entity.
     */
    RegionsDTO save(RegionsDTO regionsDTO);

    /**
     * Get all the regions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<RegionsDTO> findAll(Pageable pageable);


    /**
     * Get the "id" regions.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<RegionsDTO> findOne(Long id);

    /**
     * Delete the "id" regions.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
