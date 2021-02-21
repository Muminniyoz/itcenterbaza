package uz.itcenterbaza.service.mapper;


import uz.itcenterbaza.domain.*;
import uz.itcenterbaza.service.dto.EventHistoryDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link EventHistory} and its DTO {@link EventHistoryDTO}.
 */
@Mapper(componentModel = "spring", uses = {CenterMapper.class, UserMapper.class})
public interface EventHistoryMapper extends EntityMapper<EventHistoryDTO, EventHistory> {

    @Mapping(source = "center.id", target = "centerId")
    @Mapping(source = "user.id", target = "userId")
    EventHistoryDTO toDto(EventHistory eventHistory);

    @Mapping(source = "centerId", target = "center")
    @Mapping(source = "userId", target = "user")
    @Mapping(target = "removeOpenedUser", ignore = true)
    EventHistory toEntity(EventHistoryDTO eventHistoryDTO);

    default EventHistory fromId(Long id) {
        if (id == null) {
            return null;
        }
        EventHistory eventHistory = new EventHistory();
        eventHistory.setId(id);
        return eventHistory;
    }
}
