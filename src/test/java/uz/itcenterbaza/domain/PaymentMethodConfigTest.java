package uz.itcenterbaza.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import uz.itcenterbaza.web.rest.TestUtil;

public class PaymentMethodConfigTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PaymentMethodConfig.class);
        PaymentMethodConfig paymentMethodConfig1 = new PaymentMethodConfig();
        paymentMethodConfig1.setId(1L);
        PaymentMethodConfig paymentMethodConfig2 = new PaymentMethodConfig();
        paymentMethodConfig2.setId(paymentMethodConfig1.getId());
        assertThat(paymentMethodConfig1).isEqualTo(paymentMethodConfig2);
        paymentMethodConfig2.setId(2L);
        assertThat(paymentMethodConfig1).isNotEqualTo(paymentMethodConfig2);
        paymentMethodConfig1.setId(null);
        assertThat(paymentMethodConfig1).isNotEqualTo(paymentMethodConfig2);
    }
}
