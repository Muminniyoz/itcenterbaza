package uz.itcenterbaza.service.dto;

import java.time.LocalDate;
import javax.validation.constraints.*;
import java.io.Serializable;

/**
 * A DTO for the {@link uz.itcenterbaza.domain.Center} entity.
 */
public class CenterDTO implements Serializable {
    
    private Long id;

    @NotNull
    private String title;

    private String info;

    private LocalDate startDate;

    private String googleMapUrl;


    private Long modifiedById;

    private Long regionsId;

    private Long managerId;
    
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

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public String getGoogleMapUrl() {
        return googleMapUrl;
    }

    public void setGoogleMapUrl(String googleMapUrl) {
        this.googleMapUrl = googleMapUrl;
    }

    public Long getModifiedById() {
        return modifiedById;
    }

    public void setModifiedById(Long userId) {
        this.modifiedById = userId;
    }

    public Long getRegionsId() {
        return regionsId;
    }

    public void setRegionsId(Long regionsId) {
        this.regionsId = regionsId;
    }

    public Long getManagerId() {
        return managerId;
    }

    public void setManagerId(Long userId) {
        this.managerId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CenterDTO)) {
            return false;
        }

        return id != null && id.equals(((CenterDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CenterDTO{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", info='" + getInfo() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", googleMapUrl='" + getGoogleMapUrl() + "'" +
            ", modifiedById=" + getModifiedById() +
            ", regionsId=" + getRegionsId() +
            ", managerId=" + getManagerId() +
            "}";
    }
}
