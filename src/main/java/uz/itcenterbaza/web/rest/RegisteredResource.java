package uz.itcenterbaza.web.rest;

import uz.itcenterbaza.service.RegisteredService;
import uz.itcenterbaza.web.rest.errors.BadRequestAlertException;
import uz.itcenterbaza.service.dto.RegisteredDTO;
import uz.itcenterbaza.service.dto.RegisteredCriteria;
import uz.itcenterbaza.service.RegisteredQueryService;

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

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link uz.itcenterbaza.domain.Registered}.
 */
@RestController
@RequestMapping("/api")
public class RegisteredResource {

    private final Logger log = LoggerFactory.getLogger(RegisteredResource.class);

    private static final String ENTITY_NAME = "registered";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RegisteredService registeredService;

    private final RegisteredQueryService registeredQueryService;

    public RegisteredResource(RegisteredService registeredService, RegisteredQueryService registeredQueryService) {
        this.registeredService = registeredService;
        this.registeredQueryService = registeredQueryService;
    }

    /**
     * {@code POST  /registereds} : Create a new registered.
     *
     * @param registeredDTO the registeredDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new registeredDTO, or with status {@code 400 (Bad Request)} if the registered has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/registereds")
    public ResponseEntity<RegisteredDTO> createRegistered(@Valid @RequestBody RegisteredDTO registeredDTO) throws URISyntaxException {
        log.debug("REST request to save Registered : {}", registeredDTO);
        if (registeredDTO.getId() != null) {
            throw new BadRequestAlertException("A new registered cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RegisteredDTO result = registeredService.save(registeredDTO);
        return ResponseEntity.created(new URI("/api/registereds/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /registereds} : Updates an existing registered.
     *
     * @param registeredDTO the registeredDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated registeredDTO,
     * or with status {@code 400 (Bad Request)} if the registeredDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the registeredDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/registereds")
    public ResponseEntity<RegisteredDTO> updateRegistered(@Valid @RequestBody RegisteredDTO registeredDTO) throws URISyntaxException {
        log.debug("REST request to update Registered : {}", registeredDTO);
        if (registeredDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        RegisteredDTO result = registeredService.save(registeredDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, registeredDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /registereds} : get all the registereds.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of registereds in body.
     */
    @GetMapping("/registereds")
    public ResponseEntity<List<RegisteredDTO>> getAllRegistereds(RegisteredCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Registereds by criteria: {}", criteria);
        Page<RegisteredDTO> page = registeredQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /registereds/count} : count all the registereds.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/registereds/count")
    public ResponseEntity<Long> countRegistereds(RegisteredCriteria criteria) {
        log.debug("REST request to count Registereds by criteria: {}", criteria);
        return ResponseEntity.ok().body(registeredQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /registereds/:id} : get the "id" registered.
     *
     * @param id the id of the registeredDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the registeredDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/registereds/{id}")
    public ResponseEntity<RegisteredDTO> getRegistered(@PathVariable Long id) {
        log.debug("REST request to get Registered : {}", id);
        Optional<RegisteredDTO> registeredDTO = registeredService.findOne(id);
        return ResponseUtil.wrapOrNotFound(registeredDTO);
    }

    /**
     * {@code DELETE  /registereds/:id} : delete the "id" registered.
     *
     * @param id the id of the registeredDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/registereds/{id}")
    public ResponseEntity<Void> deleteRegistered(@PathVariable Long id) {
        log.debug("REST request to delete Registered : {}", id);
        registeredService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
