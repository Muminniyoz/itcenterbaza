package uz.itcenterbaza.web.rest;

import uz.itcenterbaza.service.PaymentMethodConfigService;
import uz.itcenterbaza.web.rest.errors.BadRequestAlertException;
import uz.itcenterbaza.service.dto.PaymentMethodConfigDTO;
import uz.itcenterbaza.service.dto.PaymentMethodConfigCriteria;
import uz.itcenterbaza.service.PaymentMethodConfigQueryService;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link uz.itcenterbaza.domain.PaymentMethodConfig}.
 */
@RestController
@RequestMapping("/api")
public class PaymentMethodConfigResource {

    private final Logger log = LoggerFactory.getLogger(PaymentMethodConfigResource.class);

    private static final String ENTITY_NAME = "paymentMethodConfig";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PaymentMethodConfigService paymentMethodConfigService;

    private final PaymentMethodConfigQueryService paymentMethodConfigQueryService;

    public PaymentMethodConfigResource(PaymentMethodConfigService paymentMethodConfigService, PaymentMethodConfigQueryService paymentMethodConfigQueryService) {
        this.paymentMethodConfigService = paymentMethodConfigService;
        this.paymentMethodConfigQueryService = paymentMethodConfigQueryService;
    }

    /**
     * {@code POST  /payment-method-configs} : Create a new paymentMethodConfig.
     *
     * @param paymentMethodConfigDTO the paymentMethodConfigDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new paymentMethodConfigDTO, or with status {@code 400 (Bad Request)} if the paymentMethodConfig has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/payment-method-configs")
    public ResponseEntity<PaymentMethodConfigDTO> createPaymentMethodConfig(@RequestBody PaymentMethodConfigDTO paymentMethodConfigDTO) throws URISyntaxException {
        log.debug("REST request to save PaymentMethodConfig : {}", paymentMethodConfigDTO);
        if (paymentMethodConfigDTO.getId() != null) {
            throw new BadRequestAlertException("A new paymentMethodConfig cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PaymentMethodConfigDTO result = paymentMethodConfigService.save(paymentMethodConfigDTO);
        return ResponseEntity.created(new URI("/api/payment-method-configs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /payment-method-configs} : Updates an existing paymentMethodConfig.
     *
     * @param paymentMethodConfigDTO the paymentMethodConfigDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated paymentMethodConfigDTO,
     * or with status {@code 400 (Bad Request)} if the paymentMethodConfigDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the paymentMethodConfigDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/payment-method-configs")
    public ResponseEntity<PaymentMethodConfigDTO> updatePaymentMethodConfig(@RequestBody PaymentMethodConfigDTO paymentMethodConfigDTO) throws URISyntaxException {
        log.debug("REST request to update PaymentMethodConfig : {}", paymentMethodConfigDTO);
        if (paymentMethodConfigDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PaymentMethodConfigDTO result = paymentMethodConfigService.save(paymentMethodConfigDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, paymentMethodConfigDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /payment-method-configs} : get all the paymentMethodConfigs.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of paymentMethodConfigs in body.
     */
    @GetMapping("/payment-method-configs")
    public ResponseEntity<List<PaymentMethodConfigDTO>> getAllPaymentMethodConfigs(PaymentMethodConfigCriteria criteria, Pageable pageable) {
        log.debug("REST request to get PaymentMethodConfigs by criteria: {}", criteria);
        Page<PaymentMethodConfigDTO> page = paymentMethodConfigQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /payment-method-configs/count} : count all the paymentMethodConfigs.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/payment-method-configs/count")
    public ResponseEntity<Long> countPaymentMethodConfigs(PaymentMethodConfigCriteria criteria) {
        log.debug("REST request to count PaymentMethodConfigs by criteria: {}", criteria);
        return ResponseEntity.ok().body(paymentMethodConfigQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /payment-method-configs/:id} : get the "id" paymentMethodConfig.
     *
     * @param id the id of the paymentMethodConfigDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the paymentMethodConfigDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/payment-method-configs/{id}")
    public ResponseEntity<PaymentMethodConfigDTO> getPaymentMethodConfig(@PathVariable Long id) {
        log.debug("REST request to get PaymentMethodConfig : {}", id);
        Optional<PaymentMethodConfigDTO> paymentMethodConfigDTO = paymentMethodConfigService.findOne(id);
        return ResponseUtil.wrapOrNotFound(paymentMethodConfigDTO);
    }

    /**
     * {@code DELETE  /payment-method-configs/:id} : delete the "id" paymentMethodConfig.
     *
     * @param id the id of the paymentMethodConfigDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/payment-method-configs/{id}")
    public ResponseEntity<Void> deletePaymentMethodConfig(@PathVariable Long id) {
        log.debug("REST request to delete PaymentMethodConfig : {}", id);
        paymentMethodConfigService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
