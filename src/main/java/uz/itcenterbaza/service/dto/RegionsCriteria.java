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

/**
 * Criteria class for the {@link uz.itcenterbaza.domain.Regions} entity. This class is used
 * in {@link uz.itcenterbaza.web.rest.RegionsResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /regions?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class RegionsCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter title;

    private StringFilter info;

    private StringFilter googleUrl;

    private LongFilter directorId;

    public RegionsCriteria() {
    }

    public RegionsCriteria(RegionsCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.title = other.title == null ? null : other.title.copy();
        this.info = other.info == null ? null : other.info.copy();
        this.googleUrl = other.googleUrl == null ? null : other.googleUrl.copy();
        this.directorId = other.directorId == null ? null : other.directorId.copy();
    }

    @Override
    public RegionsCriteria copy() {
        return new RegionsCriteria(this);
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

    public StringFilter getGoogleUrl() {
        return googleUrl;
    }

    public void setGoogleUrl(StringFilter googleUrl) {
        this.googleUrl = googleUrl;
    }

    public LongFilter getDirectorId() {
        return directorId;
    }

    public void setDirectorId(LongFilter directorId) {
        this.directorId = directorId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final RegionsCriteria that = (RegionsCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(title, that.title) &&
            Objects.equals(info, that.info) &&
            Objects.equals(googleUrl, that.googleUrl) &&
            Objects.equals(directorId, that.directorId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        title,
        info,
        googleUrl,
        directorId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RegionsCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (title != null ? "title=" + title + ", " : "") +
                (info != null ? "info=" + info + ", " : "") +
                (googleUrl != null ? "googleUrl=" + googleUrl + ", " : "") +
                (directorId != null ? "directorId=" + directorId + ", " : "") +
            "}";
    }

}
