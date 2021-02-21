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
 * Criteria class for the {@link uz.itcenterbaza.domain.PaymentMethod} entity. This class is used
 * in {@link uz.itcenterbaza.web.rest.PaymentMethodResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /payment-methods?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class PaymentMethodCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter paymentMethod;

    private StringFilter description;

    private BooleanFilter active;

    private LongFilter confId;

    public PaymentMethodCriteria() {
    }

    public PaymentMethodCriteria(PaymentMethodCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.paymentMethod = other.paymentMethod == null ? null : other.paymentMethod.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.active = other.active == null ? null : other.active.copy();
        this.confId = other.confId == null ? null : other.confId.copy();
    }

    @Override
    public PaymentMethodCriteria copy() {
        return new PaymentMethodCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(StringFilter paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public StringFilter getDescription() {
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public BooleanFilter getActive() {
        return active;
    }

    public void setActive(BooleanFilter active) {
        this.active = active;
    }

    public LongFilter getConfId() {
        return confId;
    }

    public void setConfId(LongFilter confId) {
        this.confId = confId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final PaymentMethodCriteria that = (PaymentMethodCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(paymentMethod, that.paymentMethod) &&
            Objects.equals(description, that.description) &&
            Objects.equals(active, that.active) &&
            Objects.equals(confId, that.confId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        paymentMethod,
        description,
        active,
        confId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PaymentMethodCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (paymentMethod != null ? "paymentMethod=" + paymentMethod + ", " : "") +
                (description != null ? "description=" + description + ", " : "") +
                (active != null ? "active=" + active + ", " : "") +
                (confId != null ? "confId=" + confId + ", " : "") +
            "}";
    }

}
