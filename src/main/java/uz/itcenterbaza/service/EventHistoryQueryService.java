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

import uz.itcenterbaza.domain.EventHistory;
import uz.itcenterbaza.domain.*; // for static metamodels
import uz.itcenterbaza.repository.EventHistoryRepository;
import uz.itcenterbaza.service.dto.EventHistoryCriteria;
import uz.itcenterbaza.service.dto.EventHistoryDTO;
import uz.itcenterbaza.service.mapper.EventHistoryMapper;

/**
 * Service for executing complex queries for {@link EventHistory} entities in the database.
 * The main input is a {@link EventHistoryCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link EventHistoryDTO} or a {@link Page} of {@link EventHistoryDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class EventHistoryQueryService extends QueryService<EventHistory> {

    private final Logger log = LoggerFactory.getLogger(EventHistoryQueryService.class);

    private final EventHistoryRepository eventHistoryRepository;

    private final EventHistoryMapper eventHistoryMapper;

    public EventHistoryQueryService(EventHistoryRepository eventHistoryRepository, EventHistoryMapper eventHistoryMapper) {
        this.eventHistoryRepository = eventHistoryRepository;
        this.eventHistoryMapper = eventHistoryMapper;
    }

    /**
     * Return a {@link List} of {@link EventHistoryDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<EventHistoryDTO> findByCriteria(EventHistoryCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<EventHistory> specification = createSpecification(criteria);
        return eventHistoryMapper.toDto(eventHistoryRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link EventHistoryDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<EventHistoryDTO> findByCriteria(EventHistoryCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<EventHistory> specification = createSpecification(criteria);
        return eventHistoryRepository.findAll(specification, page)
            .map(eventHistoryMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(EventHistoryCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<EventHistory> specification = createSpecification(criteria);
        return eventHistoryRepository.count(specification);
    }

    /**
     * Function to convert {@link EventHistoryCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<EventHistory> createSpecification(EventHistoryCriteria criteria) {
        Specification<EventHistory> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), EventHistory_.id));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildSpecification(criteria.getType(), EventHistory_.type));
            }
            if (criteria.getText() != null) {
                specification = specification.and(buildStringSpecification(criteria.getText(), EventHistory_.text));
            }
            if (criteria.getTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTime(), EventHistory_.time));
            }
            if (criteria.getCenterId() != null) {
                specification = specification.and(buildSpecification(criteria.getCenterId(),
                    root -> root.join(EventHistory_.center, JoinType.LEFT).get(Center_.id)));
            }
            if (criteria.getUserId() != null) {
                specification = specification.and(buildSpecification(criteria.getUserId(),
                    root -> root.join(EventHistory_.user, JoinType.LEFT).get(User_.id)));
            }
            if (criteria.getOpenedUserId() != null) {
                specification = specification.and(buildSpecification(criteria.getOpenedUserId(),
                    root -> root.join(EventHistory_.openedUsers, JoinType.LEFT).get(User_.id)));
            }
        }
        return specification;
    }
}
