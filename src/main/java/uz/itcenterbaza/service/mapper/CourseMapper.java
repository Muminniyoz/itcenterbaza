package uz.itcenterbaza.service.mapper;


import uz.itcenterbaza.domain.*;
import uz.itcenterbaza.service.dto.CourseDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Course} and its DTO {@link CourseDTO}.
 */
@Mapper(componentModel = "spring", uses = {TeacherMapper.class, CenterMapper.class, SkillMapper.class})
public interface CourseMapper extends EntityMapper<CourseDTO, Course> {

    @Mapping(source = "teacher.id", target = "teacherId")
    @Mapping(source = "center.id", target = "centerId")
    @Mapping(source = "skill.id", target = "skillId")
    CourseDTO toDto(Course course);

    @Mapping(target = "registereds", ignore = true)
    @Mapping(target = "removeRegistered", ignore = true)
    @Mapping(source = "teacherId", target = "teacher")
    @Mapping(source = "centerId", target = "center")
    @Mapping(source = "skillId", target = "skill")
    Course toEntity(CourseDTO courseDTO);

    default Course fromId(Long id) {
        if (id == null) {
            return null;
        }
        Course course = new Course();
        course.setId(id);
        return course;
    }
}
