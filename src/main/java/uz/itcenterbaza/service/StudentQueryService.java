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

import uz.itcenterbaza.domain.Student;
import uz.itcenterbaza.domain.*; // for static metamodels
import uz.itcenterbaza.repository.StudentRepository;
import uz.itcenterbaza.service.dto.StudentCriteria;
import uz.itcenterbaza.service.dto.StudentDTO;
import uz.itcenterbaza.service.mapper.StudentMapper;

/**
 * Service for executing complex queries for {@link Student} entities in the database.
 * The main input is a {@link StudentCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link StudentDTO} or a {@link Page} of {@link StudentDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class StudentQueryService extends QueryService<Student> {

    private final Logger log = LoggerFactory.getLogger(StudentQueryService.class);

    private final StudentRepository studentRepository;

    private final StudentMapper studentMapper;

    public StudentQueryService(StudentRepository studentRepository, StudentMapper studentMapper) {
        this.studentRepository = studentRepository;
        this.studentMapper = studentMapper;
    }

    /**
     * Return a {@link List} of {@link StudentDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<StudentDTO> findByCriteria(StudentCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Student> specification = createSpecification(criteria);
        return studentMapper.toDto(studentRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link StudentDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<StudentDTO> findByCriteria(StudentCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Student> specification = createSpecification(criteria);
        return studentRepository.findAll(specification, page)
            .map(studentMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(StudentCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Student> specification = createSpecification(criteria);
        return studentRepository.count(specification);
    }

    /**
     * Function to convert {@link StudentCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Student> createSpecification(StudentCriteria criteria) {
        Specification<Student> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Student_.id));
            }
            if (criteria.getFirstName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFirstName(), Student_.firstName));
            }
            if (criteria.getLastName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastName(), Student_.lastName));
            }
            if (criteria.getMiddleName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getMiddleName(), Student_.middleName));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), Student_.email));
            }
            if (criteria.getDateOfBirth() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateOfBirth(), Student_.dateOfBirth));
            }
            if (criteria.getGender() != null) {
                specification = specification.and(buildSpecification(criteria.getGender(), Student_.gender));
            }
            if (criteria.getRegisterationDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getRegisterationDate(), Student_.registerationDate));
            }
            if (criteria.getTelephone() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTelephone(), Student_.telephone));
            }
            if (criteria.getMobile() != null) {
                specification = specification.and(buildStringSpecification(criteria.getMobile(), Student_.mobile));
            }
            if (criteria.getThumbnailPhotoUrl() != null) {
                specification = specification.and(buildStringSpecification(criteria.getThumbnailPhotoUrl(), Student_.thumbnailPhotoUrl));
            }
            if (criteria.getFullPhotoUrl() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFullPhotoUrl(), Student_.fullPhotoUrl));
            }
            if (criteria.getIdNumber() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getIdNumber(), Student_.idNumber));
            }
            if (criteria.getModifiedById() != null) {
                specification = specification.and(buildSpecification(criteria.getModifiedById(),
                    root -> root.join(Student_.modifiedBy, JoinType.LEFT).get(User_.id)));
            }
        }
        return specification;
    }
}
