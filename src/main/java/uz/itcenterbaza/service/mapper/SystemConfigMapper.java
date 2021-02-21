package uz.itcenterbaza.service.mapper;


import uz.itcenterbaza.domain.*;
import uz.itcenterbaza.service.dto.SystemConfigDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link SystemConfig} and its DTO {@link SystemConfigDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface SystemConfigMapper extends EntityMapper<SystemConfigDTO, SystemConfig> {



    default SystemConfig fromId(Long id) {
        if (id == null) {
            return null;
        }
        SystemConfig systemConfig = new SystemConfig();
        systemConfig.setId(id);
        return systemConfig;
    }
}
