package uz.itcenterbaza.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A PaymentMethod.
 */
@Entity
@Table(name = "payment_method")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class PaymentMethod implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "payment_method", nullable = false)
    private String paymentMethod;

    @Column(name = "description")
    private String description;

    @Column(name = "active")
    private Boolean active;

    @OneToMany(mappedBy = "method")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<PaymentMethodConfig> confs = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public PaymentMethod paymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
        return this;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getDescription() {
        return description;
    }

    public PaymentMethod description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean isActive() {
        return active;
    }

    public PaymentMethod active(Boolean active) {
        this.active = active;
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Set<PaymentMethodConfig> getConfs() {
        return confs;
    }

    public PaymentMethod confs(Set<PaymentMethodConfig> paymentMethodConfigs) {
        this.confs = paymentMethodConfigs;
        return this;
    }

    public PaymentMethod addConf(PaymentMethodConfig paymentMethodConfig) {
        this.confs.add(paymentMethodConfig);
        paymentMethodConfig.setMethod(this);
        return this;
    }

    public PaymentMethod removeConf(PaymentMethodConfig paymentMethodConfig) {
        this.confs.remove(paymentMethodConfig);
        paymentMethodConfig.setMethod(null);
        return this;
    }

    public void setConfs(Set<PaymentMethodConfig> paymentMethodConfigs) {
        this.confs = paymentMethodConfigs;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PaymentMethod)) {
            return false;
        }
        return id != null && id.equals(((PaymentMethod) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PaymentMethod{" +
            "id=" + getId() +
            ", paymentMethod='" + getPaymentMethod() + "'" +
            ", description='" + getDescription() + "'" +
            ", active='" + isActive() + "'" +
            "}";
    }
}
