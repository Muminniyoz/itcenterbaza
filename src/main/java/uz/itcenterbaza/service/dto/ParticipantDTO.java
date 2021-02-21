package uz.itcenterbaza.service.dto;

import java.time.LocalDate;
import java.io.Serializable;
import uz.itcenterbaza.domain.enumeration.ParticipantStatus;

/**
 * A DTO for the {@link uz.itcenterbaza.domain.Participant} entity.
 */
public class ParticipantDTO implements Serializable {
    
    private Long id;

    private LocalDate startingDate;

    private Boolean active;

    private ParticipantStatus status;

    private String contractNumber;


    private Long studentId;

    private Long courseId;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getStartingDate() {
        return startingDate;
    }

    public void setStartingDate(LocalDate startingDate) {
        this.startingDate = startingDate;
    }

    public Boolean isActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public ParticipantStatus getStatus() {
        return status;
    }

    public void setStatus(ParticipantStatus status) {
        this.status = status;
    }

    public String getContractNumber() {
        return contractNumber;
    }

    public void setContractNumber(String contractNumber) {
        this.contractNumber = contractNumber;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ParticipantDTO)) {
            return false;
        }

        return id != null && id.equals(((ParticipantDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ParticipantDTO{" +
            "id=" + getId() +
            ", startingDate='" + getStartingDate() + "'" +
            ", active='" + isActive() + "'" +
            ", status='" + getStatus() + "'" +
            ", contractNumber='" + getContractNumber() + "'" +
            ", studentId=" + getStudentId() +
            ", courseId=" + getCourseId() +
            "}";
    }
}
