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
 * Criteria class for the {@link uz.itcenterbaza.domain.PaymentMethodConfig} entity. This class is used
 * in {@link uz.itcenterbaza.web.rest.PaymentMethodConfigResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /payment-method-configs?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class PaymentMethodConfigCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter key;

    private StringFilter value;

    private StringFilter note;

    private BooleanFilter enabled;

    private LongFilter methodId;

    public PaymentMethodConfigCriteria() {
    }

    public PaymentMethodConfigCriteria(PaymentMethodConfigCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.key = other.key == null ? null : other.key.copy();
        this.value = other.value == null ? null : other.value.copy();
        this.note = other.note == null ? null : other.note.copy();
        this.enabled = other.enabled == null ? null : other.enabled.copy();
        this.methodId = other.methodId == null ? null : other.methodId.copy();
    }

    @Override
    public PaymentMethodConfigCriteria copy() {
        return new PaymentMethodConfigCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getKey() {
        return key;
    }

    public void setKey(StringFilter key) {
        this.key = key;
    }

    public StringFilter getValue() {
        return value;
    }

    public void setValue(StringFilter value) {
        this.value = value;
    }

    public StringFilter getNote() {
        return note;
    }

    public void setNote(StringFilter note) {
        this.note = note;
    }

    public BooleanFilter getEnabled() {
        return enabled;
    }

    public void setEnabled(BooleanFilter enabled) {
        this.enabled = enabled;
    }

    public LongFilter getMethodId() {
        return methodId;
    }

    public void setMethodId(LongFilter methodId) {
        this.methodId = methodId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final PaymentMethodConfigCriteria that = (PaymentMethodConfigCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(key, that.key) &&
            Objects.equals(value, that.value) &&
            Objects.equals(note, that.note) &&
            Objects.equals(enabled, that.enabled) &&
            Objects.equals(methodId, that.methodId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        key,
        value,
        note,
        enabled,
        methodId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PaymentMethodConfigCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (key != null ? "key=" + key + ", " : "") +
                (value != null ? "value=" + value + ", " : "") +
                (note != null ? "note=" + note + ", " : "") +
                (enabled != null ? "enabled=" + enabled + ", " : "") +
                (methodId != null ? "methodId=" + methodId + ", " : "") +
            "}";
    }

}
