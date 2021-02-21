package uz.itcenterbaza.service.mapper;


import uz.itcenterbaza.domain.*;
import uz.itcenterbaza.service.dto.RegionsDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Regions} and its DTO {@link RegionsDTO}.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface RegionsMapper extends EntityMapper<RegionsDTO, Regions> {

    @Mapping(source = "director.id", target = "directorId")
    RegionsDTO toDto(Regions regions);

    @Mapping(source = "directorId", target = "director")
    Regions toEntity(RegionsDTO regionsDTO);

    default Regions fromId(Long id) {
        if (id == null) {
            return null;
        }
        Regions regions = new Regions();
        regions.setId(id);
        return regions;
    }
}
