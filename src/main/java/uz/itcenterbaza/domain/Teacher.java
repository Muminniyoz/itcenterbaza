package uz.itcenterbaza.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import uz.itcenterbaza.domain.enumeration.Gender;

/**
 * A Teacher.
 */
@Entity
@Table(name = "teacher")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Teacher implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @NotNull
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "middle_name")
    private String middleName;

    @Column(name = "email")
    private String email;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;

    @Column(name = "registeration_date")
    private LocalDate registerationDate;

    @Column(name = "telephone")
    private String telephone;

    @Column(name = "mobile")
    private String mobile;

    @Column(name = "full_photo_url")
    private String fullPhotoUrl;

    @Column(name = "active")
    private Boolean active;

    @Column(name = "key")
    private String key;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "about")
    private String about;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "portfolia")
    private String portfolia;

    @Lob
    @Column(name = "info")
    private byte[] info;

    @Column(name = "info_content_type")
    private String infoContentType;

    @Column(name = "leave_date")
    private LocalDate leaveDate;

    @Column(name = "is_showing_home")
    private Boolean isShowingHome;

    @Lob
    @Column(name = "image")
    private byte[] image;

    @Column(name = "image_content_type")
    private String imageContentType;

    @ManyToOne
    @JsonIgnoreProperties(value = "teachers", allowSetters = true)
    private User modifiedBy;

    @ManyToOne
    @JsonIgnoreProperties(value = "teachers", allowSetters = true)
    private User user;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(name = "teacher_skills",
               joinColumns = @JoinColumn(name = "teacher_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "skills_id", referencedColumnName = "id"))
    private Set<Skill> skills = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public Teacher firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Teacher lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public Teacher middleName(String middleName) {
        this.middleName = middleName;
        return this;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getEmail() {
        return email;
    }

    public Teacher email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public Teacher dateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
        return this;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Gender getGender() {
        return gender;
    }

    public Teacher gender(Gender gender) {
        this.gender = gender;
        return this;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public LocalDate getRegisterationDate() {
        return registerationDate;
    }

    public Teacher registerationDate(LocalDate registerationDate) {
        this.registerationDate = registerationDate;
        return this;
    }

    public void setRegisterationDate(LocalDate registerationDate) {
        this.registerationDate = registerationDate;
    }

    public String getTelephone() {
        return telephone;
    }

    public Teacher telephone(String telephone) {
        this.telephone = telephone;
        return this;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getMobile() {
        return mobile;
    }

    public Teacher mobile(String mobile) {
        this.mobile = mobile;
        return this;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getFullPhotoUrl() {
        return fullPhotoUrl;
    }

    public Teacher fullPhotoUrl(String fullPhotoUrl) {
        this.fullPhotoUrl = fullPhotoUrl;
        return this;
    }

    public void setFullPhotoUrl(String fullPhotoUrl) {
        this.fullPhotoUrl = fullPhotoUrl;
    }

    public Boolean isActive() {
        return active;
    }

    public Teacher active(Boolean active) {
        this.active = active;
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getKey() {
        return key;
    }

    public Teacher key(String key) {
        this.key = key;
        return this;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getAbout() {
        return about;
    }

    public Teacher about(String about) {
        this.about = about;
        return this;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getPortfolia() {
        return portfolia;
    }

    public Teacher portfolia(String portfolia) {
        this.portfolia = portfolia;
        return this;
    }

    public void setPortfolia(String portfolia) {
        this.portfolia = portfolia;
    }

    public byte[] getInfo() {
        return info;
    }

    public Teacher info(byte[] info) {
        this.info = info;
        return this;
    }

    public void setInfo(byte[] info) {
        this.info = info;
    }

    public String getInfoContentType() {
        return infoContentType;
    }

    public Teacher infoContentType(String infoContentType) {
        this.infoContentType = infoContentType;
        return this;
    }

    public void setInfoContentType(String infoContentType) {
        this.infoContentType = infoContentType;
    }

    public LocalDate getLeaveDate() {
        return leaveDate;
    }

    public Teacher leaveDate(LocalDate leaveDate) {
        this.leaveDate = leaveDate;
        return this;
    }

    public void setLeaveDate(LocalDate leaveDate) {
        this.leaveDate = leaveDate;
    }

    public Boolean isIsShowingHome() {
        return isShowingHome;
    }

    public Teacher isShowingHome(Boolean isShowingHome) {
        this.isShowingHome = isShowingHome;
        return this;
    }

    public void setIsShowingHome(Boolean isShowingHome) {
        this.isShowingHome = isShowingHome;
    }

    public byte[] getImage() {
        return image;
    }

    public Teacher image(byte[] image) {
        this.image = image;
        return this;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getImageContentType() {
        return imageContentType;
    }

    public Teacher imageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
        return this;
    }

    public void setImageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
    }

    public User getModifiedBy() {
        return modifiedBy;
    }

    public Teacher modifiedBy(User user) {
        this.modifiedBy = user;
        return this;
    }

    public void setModifiedBy(User user) {
        this.modifiedBy = user;
    }

    public User getUser() {
        return user;
    }

    public Teacher user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Skill> getSkills() {
        return skills;
    }

    public Teacher skills(Set<Skill> skills) {
        this.skills = skills;
        return this;
    }

    public Teacher addSkills(Skill skill) {
        this.skills.add(skill);
        skill.getTeachers().add(this);
        return this;
    }

    public Teacher removeSkills(Skill skill) {
        this.skills.remove(skill);
        skill.getTeachers().remove(this);
        return this;
    }

    public void setSkills(Set<Skill> skills) {
        this.skills = skills;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Teacher)) {
            return false;
        }
        return id != null && id.equals(((Teacher) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Teacher{" +
            "id=" + getId() +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", middleName='" + getMiddleName() + "'" +
            ", email='" + getEmail() + "'" +
            ", dateOfBirth='" + getDateOfBirth() + "'" +
            ", gender='" + getGender() + "'" +
            ", registerationDate='" + getRegisterationDate() + "'" +
            ", telephone='" + getTelephone() + "'" +
            ", mobile='" + getMobile() + "'" +
            ", fullPhotoUrl='" + getFullPhotoUrl() + "'" +
            ", active='" + isActive() + "'" +
            ", key='" + getKey() + "'" +
            ", about='" + getAbout() + "'" +
            ", portfolia='" + getPortfolia() + "'" +
            ", info='" + getInfo() + "'" +
            ", infoContentType='" + getInfoContentType() + "'" +
            ", leaveDate='" + getLeaveDate() + "'" +
            ", isShowingHome='" + isIsShowingHome() + "'" +
            ", image='" + getImage() + "'" +
            ", imageContentType='" + getImageContentType() + "'" +
            "}";
    }
}
