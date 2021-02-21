package uz.itcenterbaza.repository;

import uz.itcenterbaza.domain.Teacher;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Teacher entity.
 */
@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long>, JpaSpecificationExecutor<Teacher> {

    @Query("select teacher from Teacher teacher where teacher.modifiedBy.login = ?#{principal.username}")
    List<Teacher> findByModifiedByIsCurrentUser();

    @Query("select teacher from Teacher teacher where teacher.user.login = ?#{principal.username}")
    List<Teacher> findByUserIsCurrentUser();

    @Query(value = "select distinct teacher from Teacher teacher left join fetch teacher.skills",
        countQuery = "select count(distinct teacher) from Teacher teacher")
    Page<Teacher> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct teacher from Teacher teacher left join fetch teacher.skills")
    List<Teacher> findAllWithEagerRelationships();

    @Query("select teacher from Teacher teacher left join fetch teacher.skills where teacher.id =:id")
    Optional<Teacher> findOneWithEagerRelationships(@Param("id") Long id);
}
