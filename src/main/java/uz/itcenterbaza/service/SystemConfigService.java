package uz.itcenterbaza.service;

import uz.itcenterbaza.service.dto.SystemConfigDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link uz.itcenterbaza.domain.SystemConfig}.
 */
public interface SystemConfigService {

    /**
     * Save a systemConfig.
     *
     * @param systemConfigDTO the entity to save.
     * @return the persisted entity.
     */
    SystemConfigDTO save(SystemConfigDTO systemConfigDTO);

    /**
     * Get all the systemConfigs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SystemConfigDTO> findAll(Pageable pageable);


    /**
     * Get the "id" systemConfig.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SystemConfigDTO> findOne(Long id);

    /**
     * Delete the "id" systemConfig.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
