package uz.itcenterbaza.service.dto;

import io.swagger.annotations.ApiModel;
import javax.validation.constraints.*;
import java.io.Serializable;

/**
 * A DTO for the {@link uz.itcenterbaza.domain.Regions} entity.
 */
@ApiModel(description = "Entities START")
public class RegionsDTO implements Serializable {
    
    private Long id;

    @NotNull
    private String title;

    private String info;

    private String googleUrl;


    private Long directorId;
    
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

    public String getGoogleUrl() {
        return googleUrl;
    }

    public void setGoogleUrl(String googleUrl) {
        this.googleUrl = googleUrl;
    }

    public Long getDirectorId() {
        return directorId;
    }

    public void setDirectorId(Long userId) {
        this.directorId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RegionsDTO)) {
            return false;
        }

        return id != null && id.equals(((RegionsDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RegionsDTO{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", info='" + getInfo() + "'" +
            ", googleUrl='" + getGoogleUrl() + "'" +
            ", directorId=" + getDirectorId() +
            "}";
    }
}
