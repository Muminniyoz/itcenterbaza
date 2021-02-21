package uz.itcenterbaza.service.dto;

import java.time.LocalDate;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Lob;
import uz.itcenterbaza.domain.enumeration.Gender;

/**
 * A DTO for the {@link uz.itcenterbaza.domain.Teacher} entity.
 */
public class TeacherDTO implements Serializable {
    
    private Long id;

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    private String middleName;

    private String email;

    private LocalDate dateOfBirth;

    private Gender gender;

    private LocalDate registerationDate;

    private String telephone;

    private String mobile;

    private String fullPhotoUrl;

    private Boolean active;

    private String key;

    @Lob
    private String about;

    @Lob
    private String portfolia;

    @Lob
    private byte[] info;

    private String infoContentType;
    private LocalDate leaveDate;

    private Boolean isShowingHome;

    @Lob
    private byte[] image;

    private String imageContentType;

    private Long modifiedById;

    private Long userId;
    private Set<SkillDTO> skills = new HashSet<>();
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public LocalDate getRegisterationDate() {
        return registerationDate;
    }

    public void setRegisterationDate(LocalDate registerationDate) {
        this.registerationDate = registerationDate;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getFullPhotoUrl() {
        return fullPhotoUrl;
    }

    public void setFullPhotoUrl(String fullPhotoUrl) {
        this.fullPhotoUrl = fullPhotoUrl;
    }

    public Boolean isActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getPortfolia() {
        return portfolia;
    }

    public void setPortfolia(String portfolia) {
        this.portfolia = portfolia;
    }

    public byte[] getInfo() {
        return info;
    }

    public void setInfo(byte[] info) {
        this.info = info;
    }

    public String getInfoContentType() {
        return infoContentType;
    }

    public void setInfoContentType(String infoContentType) {
        this.infoContentType = infoContentType;
    }

    public LocalDate getLeaveDate() {
        return leaveDate;
    }

    public void setLeaveDate(LocalDate leaveDate) {
        this.leaveDate = leaveDate;
    }

    public Boolean isIsShowingHome() {
        return isShowingHome;
    }

    public void setIsShowingHome(Boolean isShowingHome) {
        this.isShowingHome = isShowingHome;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getImageContentType() {
        return imageContentType;
    }

    public void setImageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
    }

    public Long getModifiedById() {
        return modifiedById;
    }

    public void setModifiedById(Long userId) {
        this.modifiedById = userId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Set<SkillDTO> getSkills() {
        return skills;
    }

    public void setSkills(Set<SkillDTO> skills) {
        this.skills = skills;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TeacherDTO)) {
            return false;
        }

        return id != null && id.equals(((TeacherDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TeacherDTO{" +
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
            ", leaveDate='" + getLeaveDate() + "'" +
            ", isShowingHome='" + isIsShowingHome() + "'" +
            ", image='" + getImage() + "'" +
            ", modifiedById=" + getModifiedById() +
            ", userId=" + getUserId() +
            ", skills='" + getSkills() + "'" +
            "}";
    }
}
