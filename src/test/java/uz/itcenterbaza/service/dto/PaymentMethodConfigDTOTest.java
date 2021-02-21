package uz.itcenterbaza.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import uz.itcenterbaza.web.rest.TestUtil;

public class PaymentMethodConfigDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PaymentMethodConfigDTO.class);
        PaymentMethodConfigDTO paymentMethodConfigDTO1 = new PaymentMethodConfigDTO();
        paymentMethodConfigDTO1.setId(1L);
        PaymentMethodConfigDTO paymentMethodConfigDTO2 = new PaymentMethodConfigDTO();
        assertThat(paymentMethodConfigDTO1).isNotEqualTo(paymentMethodConfigDTO2);
        paymentMethodConfigDTO2.setId(paymentMethodConfigDTO1.getId());
        assertThat(paymentMethodConfigDTO1).isEqualTo(paymentMethodConfigDTO2);
        paymentMethodConfigDTO2.setId(2L);
        assertThat(paymentMethodConfigDTO1).isNotEqualTo(paymentMethodConfigDTO2);
        paymentMethodConfigDTO1.setId(null);
        assertThat(paymentMethodConfigDTO1).isNotEqualTo(paymentMethodConfigDTO2);
    }
}
