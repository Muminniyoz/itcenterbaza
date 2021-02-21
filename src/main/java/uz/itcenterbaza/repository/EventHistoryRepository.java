package uz.itcenterbaza.repository;

import uz.itcenterbaza.domain.EventHistory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the EventHistory entity.
 */
@Repository
public interface EventHistoryRepository extends JpaRepository<EventHistory, Long>, JpaSpecificationExecutor<EventHistory> {

    @Query("select eventHistory from EventHistory eventHistory where eventHistory.user.login = ?#{principal.username}")
    List<EventHistory> findByUserIsCurrentUser();

    @Query(value = "select distinct eventHistory from EventHistory eventHistory left join fetch eventHistory.openedUsers",
        countQuery = "select count(distinct eventHistory) from EventHistory eventHistory")
    Page<EventHistory> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct eventHistory from EventHistory eventHistory left join fetch eventHistory.openedUsers")
    List<EventHistory> findAllWithEagerRelationships();

    @Query("select eventHistory from EventHistory eventHistory left join fetch eventHistory.openedUsers where eventHistory.id =:id")
    Optional<EventHistory> findOneWithEagerRelationships(@Param("id") Long id);
}
