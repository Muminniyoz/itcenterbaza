package uz.itcenterbaza.repository;

import uz.itcenterbaza.domain.Registered;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the Registered entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RegisteredRepository extends JpaRepository<Registered, Long>, JpaSpecificationExecutor<Registered> {

    @Query("select registered from Registered registered where registered.modifiedBy.login = ?#{principal.username}")
    List<Registered> findByModifiedByIsCurrentUser();
}
