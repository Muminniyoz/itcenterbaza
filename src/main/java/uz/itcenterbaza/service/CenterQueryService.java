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

import uz.itcenterbaza.domain.Center;
import uz.itcenterbaza.domain.*; // for static metamodels
import uz.itcenterbaza.repository.CenterRepository;
import uz.itcenterbaza.service.dto.CenterCriteria;
import uz.itcenterbaza.service.dto.CenterDTO;
import uz.itcenterbaza.service.mapper.CenterMapper;

/**
 * Service for executing complex queries for {@link Center} entities in the database.
 * The main input is a {@link CenterCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CenterDTO} or a {@link Page} of {@link CenterDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CenterQueryService extends QueryService<Center> {

    private final Logger log = LoggerFactory.getLogger(CenterQueryService.class);

    private final CenterRepository centerRepository;

    private final CenterMapper centerMapper;

    public CenterQueryService(CenterRepository centerRepository, CenterMapper centerMapper) {
        this.centerRepository = centerRepository;
        this.centerMapper = centerMapper;
    }

    /**
     * Return a {@link List} of {@link CenterDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CenterDTO> findByCriteria(CenterCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Center> specification = createSpecification(criteria);
        return centerMapper.toDto(centerRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link CenterDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CenterDTO> findByCriteria(CenterCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Center> specification = createSpecification(criteria);
        return centerRepository.findAll(specification, page)
            .map(centerMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CenterCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Center> specification = createSpecification(criteria);
        return centerRepository.count(specification);
    }

    /**
     * Function to convert {@link CenterCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Center> createSpecification(CenterCriteria criteria) {
        Specification<Center> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Center_.id));
            }
            if (criteria.getTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitle(), Center_.title));
            }
            if (criteria.getInfo() != null) {
                specification = specification.and(buildStringSpecification(criteria.getInfo(), Center_.info));
            }
            if (criteria.getStartDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStartDate(), Center_.startDate));
            }
            if (criteria.getGoogleMapUrl() != null) {
                specification = specification.and(buildStringSpecification(criteria.getGoogleMapUrl(), Center_.googleMapUrl));
            }
            if (criteria.getModifiedById() != null) {
                specification = specification.and(buildSpecification(criteria.getModifiedById(),
                    root -> root.join(Center_.modifiedBy, JoinType.LEFT).get(User_.id)));
            }
            if (criteria.getRegionsId() != null) {
                specification = specification.and(buildSpecification(criteria.getRegionsId(),
                    root -> root.join(Center_.regions, JoinType.LEFT).get(Regions_.id)));
            }
            if (criteria.getManagerId() != null) {
                specification = specification.and(buildSpecification(criteria.getManagerId(),
                    root -> root.join(Center_.manager, JoinType.LEFT).get(User_.id)));
            }
        }
        return specification;
    }
}
