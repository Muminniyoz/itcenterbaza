package uz.itcenterbaza.service.mapper;


import uz.itcenterbaza.domain.*;
import uz.itcenterbaza.service.dto.CenterDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Center} and its DTO {@link CenterDTO}.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, RegionsMapper.class})
public interface CenterMapper extends EntityMapper<CenterDTO, Center> {

    @Mapping(source = "modifiedBy.id", target = "modifiedById")
    @Mapping(source = "regions.id", target = "regionsId")
    @Mapping(source = "manager.id", target = "managerId")
    CenterDTO toDto(Center center);

    @Mapping(source = "modifiedById", target = "modifiedBy")
    @Mapping(source = "regionsId", target = "regions")
    @Mapping(source = "managerId", target = "manager")
    Center toEntity(CenterDTO centerDTO);

    default Center fromId(Long id) {
        if (id == null) {
            return null;
        }
        Center center = new Center();
        center.setId(id);
        return center;
    }
}
