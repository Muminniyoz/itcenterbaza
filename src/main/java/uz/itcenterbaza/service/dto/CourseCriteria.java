package uz.itcenterbaza.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import uz.itcenterbaza.domain.enumeration.CourseStatus;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.LocalDateFilter;

/**
 * Criteria class for the {@link uz.itcenterbaza.domain.Course} entity. This class is used
 * in {@link uz.itcenterbaza.web.rest.CourseResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /courses?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CourseCriteria implements Serializable, Criteria {
    /**
     * Class for filtering CourseStatus
     */
    public static class CourseStatusFilter extends Filter<CourseStatus> {

        public CourseStatusFilter() {
        }

        public CourseStatusFilter(CourseStatusFilter filter) {
            super(filter);
        }

        @Override
        public CourseStatusFilter copy() {
            return new CourseStatusFilter(this);
        }

    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter title;

    private FloatFilter price;

    private LocalDateFilter startDate;

    private CourseStatusFilter status;

    private IntegerFilter duration;

    private LongFilter registeredId;

    private LongFilter teacherId;

    private LongFilter centerId;

    private LongFilter skillId;

    public CourseCriteria() {
    }

    public CourseCriteria(CourseCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.title = other.title == null ? null : other.title.copy();
        this.price = other.price == null ? null : other.price.copy();
        this.startDate = other.startDate == null ? null : other.startDate.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.duration = other.duration == null ? null : other.duration.copy();
        this.registeredId = other.registeredId == null ? null : other.registeredId.copy();
        this.teacherId = other.teacherId == null ? null : other.teacherId.copy();
        this.centerId = other.centerId == null ? null : other.centerId.copy();
        this.skillId = other.skillId == null ? null : other.skillId.copy();
    }

    @Override
    public CourseCriteria copy() {
        return new CourseCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getTitle() {
        return title;
    }

    public void setTitle(StringFilter title) {
        this.title = title;
    }

    public FloatFilter getPrice() {
        return price;
    }

    public void setPrice(FloatFilter price) {
        this.price = price;
    }

    public LocalDateFilter getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateFilter startDate) {
        this.startDate = startDate;
    }

    public CourseStatusFilter getStatus() {
        return status;
    }

    public void setStatus(CourseStatusFilter status) {
        this.status = status;
    }

    public IntegerFilter getDuration() {
        return duration;
    }

    public void setDuration(IntegerFilter duration) {
        this.duration = duration;
    }

    public LongFilter getRegisteredId() {
        return registeredId;
    }

    public void setRegisteredId(LongFilter registeredId) {
        this.registeredId = registeredId;
    }

    public LongFilter getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(LongFilter teacherId) {
        this.teacherId = teacherId;
    }

    public LongFilter getCenterId() {
        return centerId;
    }

    public void setCenterId(LongFilter centerId) {
        this.centerId = centerId;
    }

    public LongFilter getSkillId() {
        return skillId;
    }

    public void setSkillId(LongFilter skillId) {
        this.skillId = skillId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final CourseCriteria that = (CourseCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(title, that.title) &&
            Objects.equals(price, that.price) &&
            Objects.equals(startDate, that.startDate) &&
            Objects.equals(status, that.status) &&
            Objects.equals(duration, that.duration) &&
            Objects.equals(registeredId, that.registeredId) &&
            Objects.equals(teacherId, that.teacherId) &&
            Objects.equals(centerId, that.centerId) &&
            Objects.equals(skillId, that.skillId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        title,
        price,
        startDate,
        status,
        duration,
        registeredId,
        teacherId,
        centerId,
        skillId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CourseCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (title != null ? "title=" + title + ", " : "") +
                (price != null ? "price=" + price + ", " : "") +
                (startDate != null ? "startDate=" + startDate + ", " : "") +
                (status != null ? "status=" + status + ", " : "") +
                (duration != null ? "duration=" + duration + ", " : "") +
                (registeredId != null ? "registeredId=" + registeredId + ", " : "") +
                (teacherId != null ? "teacherId=" + teacherId + ", " : "") +
                (centerId != null ? "centerId=" + centerId + ", " : "") +
                (skillId != null ? "skillId=" + skillId + ", " : "") +
            "}";
    }

}
