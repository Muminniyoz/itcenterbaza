package uz.itcenterbaza.repository;

import uz.itcenterbaza.domain.Center;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the Center entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CenterRepository extends JpaRepository<Center, Long>, JpaSpecificationExecutor<Center> {

    @Query("select center from Center center where center.modifiedBy.login = ?#{principal.username}")
    List<Center> findByModifiedByIsCurrentUser();

    @Query("select center from Center center where center.manager.login = ?#{principal.username}")
    List<Center> findByManagerIsCurrentUser();
}
