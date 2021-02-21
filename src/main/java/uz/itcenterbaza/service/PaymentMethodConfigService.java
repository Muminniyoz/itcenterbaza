package uz.itcenterbaza.service;

import uz.itcenterbaza.service.dto.PaymentMethodConfigDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link uz.itcenterbaza.domain.PaymentMethodConfig}.
 */
public interface PaymentMethodConfigService {

    /**
     * Save a paymentMethodConfig.
     *
     * @param paymentMethodConfigDTO the entity to save.
     * @return the persisted entity.
     */
    PaymentMethodConfigDTO save(PaymentMethodConfigDTO paymentMethodConfigDTO);

    /**
     * Get all the paymentMethodConfigs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PaymentMethodConfigDTO> findAll(Pageable pageable);


    /**
     * Get the "id" paymentMethodConfig.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PaymentMethodConfigDTO> findOne(Long id);

    /**
     * Delete the "id" paymentMethodConfig.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
