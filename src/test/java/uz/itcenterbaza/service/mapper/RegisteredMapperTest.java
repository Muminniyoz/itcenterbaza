package uz.itcenterbaza.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class RegisteredMapperTest {

    private RegisteredMapper registeredMapper;

    @BeforeEach
    public void setUp() {
        registeredMapper = new RegisteredMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(registeredMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(registeredMapper.fromId(null)).isNull();
    }
}
