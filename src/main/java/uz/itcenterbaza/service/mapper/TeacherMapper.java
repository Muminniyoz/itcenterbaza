package uz.itcenterbaza.service.mapper;


import uz.itcenterbaza.domain.*;
import uz.itcenterbaza.service.dto.TeacherDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Teacher} and its DTO {@link TeacherDTO}.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, SkillMapper.class})
public interface TeacherMapper extends EntityMapper<TeacherDTO, Teacher> {

    @Mapping(source = "modifiedBy.id", target = "modifiedById")
    @Mapping(source = "user.id", target = "userId")
    TeacherDTO toDto(Teacher teacher);

    @Mapping(source = "modifiedById", target = "modifiedBy")
    @Mapping(source = "userId", target = "user")
    @Mapping(target = "removeSkills", ignore = true)
    Teacher toEntity(TeacherDTO teacherDTO);

    default Teacher fromId(Long id) {
        if (id == null) {
            return null;
        }
        Teacher teacher = new Teacher();
        teacher.setId(id);
        return teacher;
    }
}
