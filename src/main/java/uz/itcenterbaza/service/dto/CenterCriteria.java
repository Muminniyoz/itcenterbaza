package uz.itcenterbaza.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.LocalDateFilter;

/**
 * Criteria class for the {@link uz.itcenterbaza.domain.Center} entity. This class is used
 * in {@link uz.itcenterbaza.web.rest.CenterResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /centers?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CenterCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter title;

    private StringFilter info;

    private LocalDateFilter startDate;

    private StringFilter googleMapUrl;

    private LongFilter modifiedById;

    private LongFilter regionsId;

    private LongFilter managerId;

    public CenterCriteria() {
    }

    public CenterCriteria(CenterCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.title = other.title == null ? null : other.title.copy();
        this.info = other.info == null ? null : other.info.copy();
        this.startDate = other.startDate == null ? null : other.startDate.copy();
        this.googleMapUrl = other.googleMapUrl == null ? null : other.googleMapUrl.copy();
        this.modifiedById = other.modifiedById == null ? null : other.modifiedById.copy();
        this.regionsId = other.regionsId == null ? null : other.regionsId.copy();
        this.managerId = other.managerId == null ? null : other.managerId.copy();
    }

    @Override
    public CenterCriteria copy() {
        return new CenterCriteria(this);
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

    public StringFilter getInfo() {
        return info;
    }

    public void setInfo(StringFilter info) {
        this.info = info;
    }

    public LocalDateFilter getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateFilter startDate) {
        this.startDate = startDate;
    }

    public StringFilter getGoogleMapUrl() {
        return googleMapUrl;
    }

    public void setGoogleMapUrl(StringFilter googleMapUrl) {
        this.googleMapUrl = googleMapUrl;
    }

    public LongFilter getModifiedById() {
        return modifiedById;
    }

    public void setModifiedById(LongFilter modifiedById) {
        this.modifiedById = modifiedById;
    }

    public LongFilter getRegionsId() {
        return regionsId;
    }

    public void setRegionsId(LongFilter regionsId) {
        this.regionsId = regionsId;
    }

    public LongFilter getManagerId() {
        return managerId;
    }

    public void setManagerId(LongFilter managerId) {
        this.managerId = managerId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final CenterCriteria that = (CenterCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(title, that.title) &&
            Objects.equals(info, that.info) &&
            Objects.equals(startDate, that.startDate) &&
            Objects.equals(googleMapUrl, that.googleMapUrl) &&
            Objects.equals(modifiedById, that.modifiedById) &&
            Objects.equals(regionsId, that.regionsId) &&
            Objects.equals(managerId, that.managerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        title,
        info,
        startDate,
        googleMapUrl,
        modifiedById,
        regionsId,
        managerId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CenterCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (title != null ? "title=" + title + ", " : "") +
                (info != null ? "info=" + info + ", " : "") +
                (startDate != null ? "startDate=" + startDate + ", " : "") +
                (googleMapUrl != null ? "googleMapUrl=" + googleMapUrl + ", " : "") +
                (modifiedById != null ? "modifiedById=" + modifiedById + ", " : "") +
                (regionsId != null ? "regionsId=" + regionsId + ", " : "") +
                (managerId != null ? "managerId=" + managerId + ", " : "") +
            "}";
    }

}
