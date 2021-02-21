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

import uz.itcenterbaza.domain.PaymentMethodConfig;
import uz.itcenterbaza.domain.*; // for static metamodels
import uz.itcenterbaza.repository.PaymentMethodConfigRepository;
import uz.itcenterbaza.service.dto.PaymentMethodConfigCriteria;
import uz.itcenterbaza.service.dto.PaymentMethodConfigDTO;
import uz.itcenterbaza.service.mapper.PaymentMethodConfigMapper;

/**
 * Service for executing complex queries for {@link PaymentMethodConfig} entities in the database.
 * The main input is a {@link PaymentMethodConfigCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PaymentMethodConfigDTO} or a {@link Page} of {@link PaymentMethodConfigDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PaymentMethodConfigQueryService extends QueryService<PaymentMethodConfig> {

    private final Logger log = LoggerFactory.getLogger(PaymentMethodConfigQueryService.class);

    private final PaymentMethodConfigRepository paymentMethodConfigRepository;

    private final PaymentMethodConfigMapper paymentMethodConfigMapper;

    public PaymentMethodConfigQueryService(PaymentMethodConfigRepository paymentMethodConfigRepository, PaymentMethodConfigMapper paymentMethodConfigMapper) {
        this.paymentMethodConfigRepository = paymentMethodConfigRepository;
        this.paymentMethodConfigMapper = paymentMethodConfigMapper;
    }

    /**
     * Return a {@link List} of {@link PaymentMethodConfigDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PaymentMethodConfigDTO> findByCriteria(PaymentMethodConfigCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<PaymentMethodConfig> specification = createSpecification(criteria);
        return paymentMethodConfigMapper.toDto(paymentMethodConfigRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link PaymentMethodConfigDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PaymentMethodConfigDTO> findByCriteria(PaymentMethodConfigCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<PaymentMethodConfig> specification = createSpecification(criteria);
        return paymentMethodConfigRepository.findAll(specification, page)
            .map(paymentMethodConfigMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PaymentMethodConfigCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<PaymentMethodConfig> specification = createSpecification(criteria);
        return paymentMethodConfigRepository.count(specification);
    }

    /**
     * Function to convert {@link PaymentMethodConfigCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<PaymentMethodConfig> createSpecification(PaymentMethodConfigCriteria criteria) {
        Specification<PaymentMethodConfig> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), PaymentMethodConfig_.id));
            }
            if (criteria.getKey() != null) {
                specification = specification.and(buildStringSpecification(criteria.getKey(), PaymentMethodConfig_.key));
            }
            if (criteria.getValue() != null) {
                specification = specification.and(buildStringSpecification(criteria.getValue(), PaymentMethodConfig_.value));
            }
            if (criteria.getNote() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNote(), PaymentMethodConfig_.note));
            }
            if (criteria.getEnabled() != null) {
                specification = specification.and(buildSpecification(criteria.getEnabled(), PaymentMethodConfig_.enabled));
            }
            if (criteria.getMethodId() != null) {
                specification = specification.and(buildSpecification(criteria.getMethodId(),
                    root -> root.join(PaymentMethodConfig_.method, JoinType.LEFT).get(PaymentMethod_.id)));
            }
        }
        return specification;
    }
}
