package uz.itcenterbaza.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;

import uz.itcenterbaza.domain.enumeration.PaymentStatus;

/**
 * A Payment.
 */
@Entity
@Table(name = "payment")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Payment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "payment_date")
    private ZonedDateTime paymentDate;

    @Column(name = "payment_provider")
    private String paymentProvider;

    @Column(name = "amount", precision = 21, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status")
    private PaymentStatus paymentStatus;

    @Column(name = "curency")
    private String curency;

    @Column(name = "customer_name")
    private String customerName;

    @Column(name = "is_enough")
    private Boolean isEnough;

    @Column(name = "is_confirmed")
    private Boolean isConfirmed;

    @ManyToOne
    @JsonIgnoreProperties(value = "payments", allowSetters = true)
    private User modifiedBy;

    @ManyToOne
    @JsonIgnoreProperties(value = "payments", allowSetters = true)
    private Participant student;

    @ManyToOne
    @JsonIgnoreProperties(value = "payments", allowSetters = true)
    private PaymentMethod method;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getPaymentDate() {
        return paymentDate;
    }

    public Payment paymentDate(ZonedDateTime paymentDate) {
        this.paymentDate = paymentDate;
        return this;
    }

    public void setPaymentDate(ZonedDateTime paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getPaymentProvider() {
        return paymentProvider;
    }

    public Payment paymentProvider(String paymentProvider) {
        this.paymentProvider = paymentProvider;
        return this;
    }

    public void setPaymentProvider(String paymentProvider) {
        this.paymentProvider = paymentProvider;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Payment amount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public Payment paymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
        return this;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getCurency() {
        return curency;
    }

    public Payment curency(String curency) {
        this.curency = curency;
        return this;
    }

    public void setCurency(String curency) {
        this.curency = curency;
    }

    public String getCustomerName() {
        return customerName;
    }

    public Payment customerName(String customerName) {
        this.customerName = customerName;
        return this;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Boolean isIsEnough() {
        return isEnough;
    }

    public Payment isEnough(Boolean isEnough) {
        this.isEnough = isEnough;
        return this;
    }

    public void setIsEnough(Boolean isEnough) {
        this.isEnough = isEnough;
    }

    public Boolean isIsConfirmed() {
        return isConfirmed;
    }

    public Payment isConfirmed(Boolean isConfirmed) {
        this.isConfirmed = isConfirmed;
        return this;
    }

    public void setIsConfirmed(Boolean isConfirmed) {
        this.isConfirmed = isConfirmed;
    }

    public User getModifiedBy() {
        return modifiedBy;
    }

    public Payment modifiedBy(User user) {
        this.modifiedBy = user;
        return this;
    }

    public void setModifiedBy(User user) {
        this.modifiedBy = user;
    }

    public Participant getStudent() {
        return student;
    }

    public Payment student(Participant participant) {
        this.student = participant;
        return this;
    }

    public void setStudent(Participant participant) {
        this.student = participant;
    }

    public PaymentMethod getMethod() {
        return method;
    }

    public Payment method(PaymentMethod paymentMethod) {
        this.method = paymentMethod;
        return this;
    }

    public void setMethod(PaymentMethod paymentMethod) {
        this.method = paymentMethod;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Payment)) {
            return false;
        }
        return id != null && id.equals(((Payment) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Payment{" +
            "id=" + getId() +
            ", paymentDate='" + getPaymentDate() + "'" +
            ", paymentProvider='" + getPaymentProvider() + "'" +
            ", amount=" + getAmount() +
            ", paymentStatus='" + getPaymentStatus() + "'" +
            ", curency='" + getCurency() + "'" +
            ", customerName='" + getCustomerName() + "'" +
            ", isEnough='" + isIsEnough() + "'" +
            ", isConfirmed='" + isIsConfirmed() + "'" +
            "}";
    }
}
