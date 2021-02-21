package uz.itcenterbaza.service.dto;

import java.io.Serializable;

/**
 * A DTO for the {@link uz.itcenterbaza.domain.PaymentMethodConfig} entity.
 */
public class PaymentMethodConfigDTO implements Serializable {
    
    private Long id;

    private String key;

    private String value;

    private String note;

    private Boolean enabled;


    private Long methodId;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Long getMethodId() {
        return methodId;
    }

    public void setMethodId(Long paymentMethodId) {
        this.methodId = paymentMethodId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PaymentMethodConfigDTO)) {
            return false;
        }

        return id != null && id.equals(((PaymentMethodConfigDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PaymentMethodConfigDTO{" +
            "id=" + getId() +
            ", key='" + getKey() + "'" +
            ", value='" + getValue() + "'" +
            ", note='" + getNote() + "'" +
            ", enabled='" + isEnabled() + "'" +
            ", methodId=" + getMethodId() +
            "}";
    }
}
