package uz.itcenterbaza.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import javax.persistence.Lob;

/**
 * A DTO for the {@link uz.itcenterbaza.domain.Skill} entity.
 */
public class SkillDTO implements Serializable {
    
    private Long id;

    @NotNull
    private String titleUz;

    private String titleRu;

    private String titleEn;

    @Lob
    private String about;

    @Lob
    private byte[] planFile;

    private String planFileContentType;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitleUz() {
        return titleUz;
    }

    public void setTitleUz(String titleUz) {
        this.titleUz = titleUz;
    }

    public String getTitleRu() {
        return titleRu;
    }

    public void setTitleRu(String titleRu) {
        this.titleRu = titleRu;
    }

    public String getTitleEn() {
        return titleEn;
    }

    public void setTitleEn(String titleEn) {
        this.titleEn = titleEn;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SkillDTO)) {
            return false;
        }

        return id != null && id.equals(((SkillDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SkillDTO{" +
            "id=" + getId() +
            ", titleUz='" + getTitleUz() + "'" +
            ", titleRu='" + getTitleRu() + "'" +
            ", titleEn='" + getTitleEn() + "'" +
            ", about='" + getAbout() + "'" +
            ", planFile='" + getPlanFile() + "'" +
            "}";
    }
}
