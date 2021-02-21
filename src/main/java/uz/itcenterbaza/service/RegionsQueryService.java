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

import uz.itcenterbaza.domain.Regions;
import uz.itcenterbaza.domain.*; // for static metamodels
import uz.itcenterbaza.repository.RegionsRepository;
import uz.itcenterbaza.service.dto.RegionsCriteria;
import uz.itcenterbaza.service.dto.RegionsDTO;
import uz.itcenterbaza.service.mapper.RegionsMapper;

/**
 * Service for executing complex queries for {@link Regions} entities in the database.
 * The main input is a {@link RegionsCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link RegionsDTO} or a {@link Page} of {@link RegionsDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class RegionsQueryService extends QueryService<Regions> {

    private final Logger log = LoggerFactory.getLogger(RegionsQueryService.class);

    private final RegionsRepository regionsRepository;

    private final RegionsMapper regionsMapper;

    public RegionsQueryService(RegionsRepository regionsRepository, RegionsMapper regionsMapper) {
        this.regionsRepository = regionsRepository;
        this.regionsMapper = regionsMapper;
    }

    /**
     * Return a {@link List} of {@link RegionsDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<RegionsDTO> findByCriteria(RegionsCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Regions> specification = createSpecification(criteria);
        return regionsMapper.toDto(regionsRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link RegionsDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<RegionsDTO> findByCriteria(RegionsCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Regions> specification = createSpecification(criteria);
        return regionsRepository.findAll(specification, page)
            .map(regionsMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(RegionsCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Regions> specification = createSpecification(criteria);
        return regionsRepository.count(specification);
    }

    /**
     * Function to convert {@link RegionsCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Regions> createSpecification(RegionsCriteria criteria) {
        Specification<Regions> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Regions_.id));
            }
            if (criteria.getTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitle(), Regions_.title));
            }
            if (criteria.getInfo() != null) {
                specification = specification.and(buildStringSpecification(criteria.getInfo(), Regions_.info));
            }
            if (criteria.getGoogleUrl() != null) {
                specification = specification.and(buildStringSpecification(criteria.getGoogleUrl(), Regions_.googleUrl));
            }
            if (criteria.getDirectorId() != null) {
                specification = specification.and(buildSpecification(criteria.getDirectorId(),
                    root -> root.join(Regions_.director, JoinType.LEFT).get(User_.id)));
            }
        }
        return specification;
    }
}
