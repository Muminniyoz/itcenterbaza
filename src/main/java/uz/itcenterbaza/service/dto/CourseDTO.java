package uz.itcenterbaza.service.dto;

import java.time.LocalDate;
import java.io.Serializable;
import javax.persistence.Lob;
import uz.itcenterbaza.domain.enumeration.CourseStatus;

/**
 * A DTO for the {@link uz.itcenterbaza.domain.Course} entity.
 */
public class CourseDTO implements Serializable {
    
    private Long id;

    private String title;

    private Float price;

    private LocalDate startDate;

    private CourseStatus status;

    private Integer duration;

    @Lob
    private byte[] planFile;

    private String planFileContentType;

    private Long teacherId;

    private Long centerId;

    private Long skillId;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public CourseStatus getStatus() {
        return status;
    }

    public void setStatus(CourseStatus status) {
        this.status = status;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public byte[] getPlanFile() {
        return planFile;
    }

    public void setPlanFile(byte[] planFile) {
        this.planFile = planFile;
    }

    public String getPlanFileContentType() {
        return planFileContentType;
    }

    public void setPlanFileContentType(String planFileContentType) {
        this.planFileContentType = planFileContentType;
    }

    public Long getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Long teacherId) {
        this.teacherId = teacherId;
    }

    public Long getCenterId() {
        return centerId;
    }

    public void setCenterId(Long centerId) {
        this.centerId = centerId;
    }

    public Long getSkillId() {
        return skillId;
    }

    public void setSkillId(Long skillId) {
        this.skillId = skillId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CourseDTO)) {
            return false;
        }

        return id != null && id.equals(((CourseDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CourseDTO{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", price=" + getPrice() +
            ", startDate='" + getStartDate() + "'" +
            ", status='" + getStatus() + "'" +
            ", duration=" + getDuration() +
            ", planFile='" + getPlanFile() + "'" +
            ", teacherId=" + getTeacherId() +
            ", centerId=" + getCenterId() +
            ", skillId=" + getSkillId() +
            "}";
    }
}
