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

import uz.itcenterbaza.domain.SystemConfig;
import uz.itcenterbaza.domain.*; // for static metamodels
import uz.itcenterbaza.repository.SystemConfigRepository;
import uz.itcenterbaza.service.dto.SystemConfigCriteria;
import uz.itcenterbaza.service.dto.SystemConfigDTO;
import uz.itcenterbaza.service.mapper.SystemConfigMapper;

/**
 * Service for executing complex queries for {@link SystemConfig} entities in the database.
 * The main input is a {@link SystemConfigCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link SystemConfigDTO} or a {@link Page} of {@link SystemConfigDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SystemConfigQueryService extends QueryService<SystemConfig> {

    private final Logger log = LoggerFactory.getLogger(SystemConfigQueryService.class);

    private final SystemConfigRepository systemConfigRepository;

    private final SystemConfigMapper systemConfigMapper;

    public SystemConfigQueryService(SystemConfigRepository systemConfigRepository, SystemConfigMapper systemConfigMapper) {
        this.systemConfigRepository = systemConfigRepository;
        this.systemConfigMapper = systemConfigMapper;
    }

    /**
     * Return a {@link List} of {@link SystemConfigDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<SystemConfigDTO> findByCriteria(SystemConfigCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<SystemConfig> specification = createSpecification(criteria);
        return systemConfigMapper.toDto(systemConfigRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link SystemConfigDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SystemConfigDTO> findByCriteria(SystemConfigCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<SystemConfig> specification = createSpecification(criteria);
        return systemConfigRepository.findAll(specification, page)
            .map(systemConfigMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SystemConfigCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<SystemConfig> specification = createSpecification(criteria);
        return systemConfigRepository.count(specification);
    }

    /**
     * Function to convert {@link SystemConfigCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<SystemConfig> createSpecification(SystemConfigCriteria criteria) {
        Specification<SystemConfig> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), SystemConfig_.id));
            }
            if (criteria.getKey() != null) {
                specification = specification.and(buildStringSpecification(criteria.getKey(), SystemConfig_.key));
            }
            if (criteria.getValue() != null) {
                specification = specification.and(buildStringSpecification(criteria.getValue(), SystemConfig_.value));
            }
            if (criteria.getNote() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNote(), SystemConfig_.note));
            }
            if (criteria.getEnabled() != null) {
                specification = specification.and(buildSpecification(criteria.getEnabled(), SystemConfig_.enabled));
            }
        }
        return specification;
    }
}
