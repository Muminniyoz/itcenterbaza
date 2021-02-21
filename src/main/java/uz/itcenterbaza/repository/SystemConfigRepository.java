package uz.itcenterbaza.repository;

import uz.itcenterbaza.domain.SystemConfig;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the SystemConfig entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SystemConfigRepository extends JpaRepository<SystemConfig, Long>, JpaSpecificationExecutor<SystemConfig> {
}
