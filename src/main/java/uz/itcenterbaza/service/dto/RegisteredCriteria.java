package uz.itcenterbaza.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import uz.itcenterbaza.domain.enumeration.Gender;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.LocalDateFilter;

/**
 * Criteria class for the {@link uz.itcenterbaza.domain.Registered} entity. This class is used
 * in {@link uz.itcenterbaza.web.rest.RegisteredResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /registereds?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class RegisteredCriteria implements Serializable, Criteria {
    /**
     * Class for filtering Gender
     */
    public static class GenderFilter extends Filter<Gender> {

        public GenderFilter() {
        }

        public GenderFilter(GenderFilter filter) {
            super(filter);
        }

        @Override
        public GenderFilter copy() {
            return new GenderFilter(this);
        }

    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter firstName;

    private StringFilter lastName;

    private StringFilter middleName;

    private StringFilter email;

    private LocalDateFilter dateOfBirth;

    private GenderFilter gender;

    private LocalDateFilter registerationDate;

    private StringFilter telephone;

    private StringFilter mobile;

    private LongFilter modifiedById;

    private LongFilter courseId;

    public RegisteredCriteria() {
    }

    public RegisteredCriteria(RegisteredCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.firstName = other.firstName == null ? null : other.firstName.copy();
        this.lastName = other.lastName == null ? null : other.lastName.copy();
        this.middleName = other.middleName == null ? null : other.middleName.copy();
        this.email = other.email == null ? null : other.email.copy();
        this.dateOfBirth = other.dateOfBirth == null ? null : other.dateOfBirth.copy();
        this.gender = other.gender == null ? null : other.gender.copy();
        this.registerationDate = other.registerationDate == null ? null : other.registerationDate.copy();
        this.telephone = other.telephone == null ? null : other.telephone.copy();
        this.mobile = other.mobile == null ? null : other.mobile.copy();
        this.modifiedById = other.modifiedById == null ? null : other.modifiedById.copy();
        this.courseId = other.courseId == null ? null : other.courseId.copy();
    }

    @Override
    public RegisteredCriteria copy() {
        return new RegisteredCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getFirstName() {
        return firstName;
    }

    public void setFirstName(StringFilter firstName) {
        this.firstName = firstName;
    }

    public StringFilter getLastName() {
        return lastName;
    }

    public void setLastName(StringFilter lastName) {
        this.lastName = lastName;
    }

    public StringFilter getMiddleName() {
        return middleName;
    }

    public void setMiddleName(StringFilter middleName) {
        this.middleName = middleName;
    }

    public StringFilter getEmail() {
        return email;
    }

    public void setEmail(StringFilter email) {
        this.email = email;
    }

    public LocalDateFilter getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDateFilter dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public GenderFilter getGender() {
        return gender;
    }

    public void setGender(GenderFilter gender) {
        this.gender = gender;
    }

    public LocalDateFilter getRegisterationDate() {
        return registerationDate;
    }

    public void setRegisterationDate(LocalDateFilter registerationDate) {
        this.registerationDate = registerationDate;
    }

    public StringFilter getTelephone() {
        return telephone;
    }

    public void setTelephone(StringFilter telephone) {
        this.telephone = telephone;
    }

    public StringFilter getMobile() {
        return mobile;
    }

    public void setMobile(StringFilter mobile) {
        this.mobile = mobile;
    }

    public LongFilter getModifiedById() {
        return modifiedById;
    }

    public void setModifiedById(LongFilter modifiedById) {
        this.modifiedById = modifiedById;
    }

    public LongFilter getCourseId() {
        return courseId;
    }

    public void setCourseId(LongFilter courseId) {
        this.courseId = courseId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final RegisteredCriteria that = (RegisteredCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(firstName, that.firstName) &&
            Objects.equals(lastName, that.lastName) &&
            Objects.equals(middleName, that.middleName) &&
            Objects.equals(email, that.email) &&
            Objects.equals(dateOfBirth, that.dateOfBirth) &&
            Objects.equals(gender, that.gender) &&
            Objects.equals(registerationDate, that.registerationDate) &&
            Objects.equals(telephone, that.telephone) &&
            Objects.equals(mobile, that.mobile) &&
            Objects.equals(modifiedById, that.modifiedById) &&
            Objects.equals(courseId, that.courseId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        firstName,
        lastName,
        middleName,
        email,
        dateOfBirth,
        gender,
        registerationDate,
        telephone,
        mobile,
        modifiedById,
        courseId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RegisteredCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (firstName != null ? "firstName=" + firstName + ", " : "") +
                (lastName != null ? "lastName=" + lastName + ", " : "") +
                (middleName != null ? "middleName=" + middleName + ", " : "") +
                (email != null ? "email=" + email + ", " : "") +
                (dateOfBirth != null ? "dateOfBirth=" + dateOfBirth + ", " : "") +
                (gender != null ? "gender=" + gender + ", " : "") +
                (registerationDate != null ? "registerationDate=" + registerationDate + ", " : "") +
                (telephone != null ? "telephone=" + telephone + ", " : "") +
                (mobile != null ? "mobile=" + mobile + ", " : "") +
                (modifiedById != null ? "modifiedById=" + modifiedById + ", " : "") +
                (courseId != null ? "courseId=" + courseId + ", " : "") +
            "}";
    }

}
