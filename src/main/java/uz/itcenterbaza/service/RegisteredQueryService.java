package uz.itcenterbaza.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import uz.itcenterbaza.domain.Registered;
import uz.itcenterbaza.domain.*; // for static metamodels
import uz.itcenterbaza.repository.RegisteredRepository;
import uz.itcenterbaza.service.dto.RegisteredCriteria;
import uz.itcenterbaza.service.dto.RegisteredDTO;
import uz.itcenterbaza.service.mapper.RegisteredMapper;

/**
 * Service for executing complex queries for {@link Registered} entities in the database.
 * The main input is a {@link RegisteredCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link RegisteredDTO} or a {@link Page} of {@link RegisteredDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class RegisteredQueryService extends QueryService<Registered> {

    private final Logger log = LoggerFactory.getLogger(RegisteredQueryService.class);

    private final RegisteredRepository registeredRepository;

    private final RegisteredMapper registeredMapper;

    public RegisteredQueryService(RegisteredRepository registeredRepository, RegisteredMapper registeredMapper) {
        this.registeredRepository = registeredRepository;
        this.registeredMapper = registeredMapper;
    }

    /**
     * Return a {@link List} of {@link RegisteredDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<RegisteredDTO> findByCriteria(RegisteredCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Registered> specification = createSpecification(criteria);
        return registeredMapper.toDto(registeredRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link RegisteredDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<RegisteredDTO> findByCriteria(RegisteredCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Registered> specification = createSpecification(criteria);
        return registeredRepository.findAll(specification, page)
            .map(registeredMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(RegisteredCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Registered> specification = createSpecification(criteria);
        return registeredRepository.count(specification);
    }

    /**
     * Function to convert {@link RegisteredCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Registered> createSpecification(RegisteredCriteria criteria) {
        Specification<Registered> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Registered_.id));
            }
            if (criteria.getFirstName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFirstName(), Registered_.firstName));
            }
            if (criteria.getLastName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastName(), Registered_.lastName));
            }
            if (criteria.getMiddleName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getMiddleName(), Registered_.middleName));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), Registered_.email));
            }
            if (criteria.getDateOfBirth() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateOfBirth(), Registered_.dateOfBirth));
            }
            if (criteria.getGender() != null) {
                specification = specification.and(buildSpecification(criteria.getGender(), Registered_.gender));
            }
            if (criteria.getRegisterationDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getRegisterationDate(), Registered_.registerationDate));
            }
            if (criteria.getTelephone() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTelephone(), Registered_.telephone));
            }
            if (criteria.getMobile() != null) {
                specification = specification.and(buildStringSpecification(criteria.getMobile(), Registered_.mobile));
            }
            if (criteria.getModifiedById() != null) {
                specification = specification.and(buildSpecification(criteria.getModifiedById(),
                    root -> root.join(Registered_.modifiedBy, JoinType.LEFT).get(User_.id)));
            }
            if (criteria.getCourseId() != null) {
                specification = specification.and(buildSpecification(criteria.getCourseId(),
                    root -> root.join(Registered_.course, JoinType.LEFT).get(Course_.id)));
            }
        }
        return specification;
    }
}
