package uz.itcenterbaza.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Skill.
 */
@Entity
@Table(name = "skill")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Skill implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "title_uz", nullable = false)
    private String titleUz;

    @Column(name = "title_ru")
    private String titleRu;

    @Column(name = "title_en")
    private String titleEn;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "about")
    private String about;

    @Lob
    @Column(name = "plan_file")
    private byte[] planFile;

    @Column(name = "plan_file_content_type")
    private String planFileContentType;

    @ManyToMany(mappedBy = "skills")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnore
    private Set<Teacher> teachers = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitleUz() {
        return titleUz;
    }

    public Skill titleUz(String titleUz) {
        this.titleUz = titleUz;
        return this;
    }

    public void setTitleUz(String titleUz) {
        this.titleUz = titleUz;
    }

    public String getTitleRu() {
        return titleRu;
    }

    public Skill titleRu(String titleRu) {
        this.titleRu = titleRu;
        return this;
    }

    public void setTitleRu(String titleRu) {
        this.titleRu = titleRu;
    }

    public String getTitleEn() {
        return titleEn;
    }

    public Skill titleEn(String titleEn) {
        this.titleEn = titleEn;
        return this;
    }

    public void setTitleEn(String titleEn) {
        this.titleEn = titleEn;
    }

    public String getAbout() {
        return about;
    }

    public Skill about(String about) {
        this.about = about;
        return this;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public byte[] getPlanFile() {
        return planFile;
    }

    public Skill planFile(byte[] planFile) {
        this.planFile = planFile;
        return this;
    }

    public void setPlanFile(byte[] planFile) {
        this.planFile = planFile;
    }

    public String getPlanFileContentType() {
        return planFileContentType;
    }

    public Skill planFileContentType(String planFileContentType) {
        this.planFileContentType = planFileContentType;
        return this;
    }

    public void setPlanFileContentType(String planFileContentType) {
        this.planFileContentType = planFileContentType;
    }

    public Set<Teacher> getTeachers() {
        return teachers;
    }

    public Skill teachers(Set<Teacher> teachers) {
        this.teachers = teachers;
        return this;
    }

    public Skill addTeacher(Teacher teacher) {
        this.teachers.add(teacher);
        teacher.getSkills().add(this);
        return this;
    }

    public Skill removeTeacher(Teacher teacher) {
        this.teachers.remove(teacher);
        teacher.getSkills().remove(this);
        return this;
    }

    public void setTeachers(Set<Teacher> teachers) {
        this.teachers = teachers;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Skill)) {
            return false;
        }
        return id != null && id.equals(((Skill) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Skill{" +
            "id=" + getId() +
            ", titleUz='" + getTitleUz() + "'" +
            ", titleRu='" + getTitleRu() + "'" +
            ", titleEn='" + getTitleEn() + "'" +
            ", about='" + getAbout() + "'" +
            ", planFile='" + getPlanFile() + "'" +
            ", planFileContentType='" + getPlanFileContentType() + "'" +
            "}";
    }
}
