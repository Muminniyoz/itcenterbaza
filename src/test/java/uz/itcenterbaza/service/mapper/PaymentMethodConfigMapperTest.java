package uz.itcenterbaza.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class PaymentMethodConfigMapperTest {

    private PaymentMethodConfigMapper paymentMethodConfigMapper;

    @BeforeEach
    public void setUp() {
        paymentMethodConfigMapper = new PaymentMethodConfigMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(paymentMethodConfigMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(paymentMethodConfigMapper.fromId(null)).isNull();
    }
}
