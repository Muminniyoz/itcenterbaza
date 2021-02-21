package uz.itcenterbaza.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class PaymentMethodMapperTest {

    private PaymentMethodMapper paymentMethodMapper;

    @BeforeEach
    public void setUp() {
        paymentMethodMapper = new PaymentMethodMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(paymentMethodMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(paymentMethodMapper.fromId(null)).isNull();
    }
}
