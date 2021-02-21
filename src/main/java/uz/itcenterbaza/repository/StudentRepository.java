package uz.itcenterbaza.repository;

import uz.itcenterbaza.domain.Student;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the Student entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StudentRepository extends JpaRepository<Student, Long>, JpaSpecificationExecutor<Student> {

    @Query("select student from Student student where student.modifiedBy.login = ?#{principal.username}")
    List<Student> findByModifiedByIsCurrentUser();
}
