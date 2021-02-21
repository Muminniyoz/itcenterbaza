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
 * Criteria class for the {@link uz.itcenterbaza.domain.Skill} entity. This class is used
 * in {@link uz.itcenterbaza.web.rest.SkillResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /skills?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class SkillCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter titleUz;

    private StringFilter titleRu;

    private StringFilter titleEn;

    private LongFilter teacherId;

    public SkillCriteria() {
    }

    public SkillCriteria(SkillCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.titleUz = other.titleUz == null ? null : other.titleUz.copy();
        this.titleRu = other.titleRu == null ? null : other.titleRu.copy();
        this.titleEn = other.titleEn == null ? null : other.titleEn.copy();
        this.teacherId = other.teacherId == null ? null : other.teacherId.copy();
    }

    @Override
    public SkillCriteria copy() {
        return new SkillCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getTitleUz() {
        return titleUz;
    }

    public void setTitleUz(StringFilter titleUz) {
        this.titleUz = titleUz;
    }

    public StringFilter getTitleRu() {
        return titleRu;
    }

    public void setTitleRu(StringFilter titleRu) {
        this.titleRu = titleRu;
    }

    public StringFilter getTitleEn() {
        return titleEn;
    }

    public void setTitleEn(StringFilter titleEn) {
        this.titleEn = titleEn;
    }

    public LongFilter getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(LongFilter teacherId) {
        this.teacherId = teacherId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final SkillCriteria that = (SkillCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(titleUz, that.titleUz) &&
            Objects.equals(titleRu, that.titleRu) &&
            Objects.equals(titleEn, that.titleEn) &&
            Objects.equals(teacherId, that.teacherId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        titleUz,
        titleRu,
        titleEn,
        teacherId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SkillCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (titleUz != null ? "titleUz=" + titleUz + ", " : "") +
                (titleRu != null ? "titleRu=" + titleRu + ", " : "") +
                (titleEn != null ? "titleEn=" + titleEn + ", " : "") +
                (teacherId != null ? "teacherId=" + teacherId + ", " : "") +
            "}";
    }

}
