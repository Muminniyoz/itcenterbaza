package uz.itcenterbaza.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class RegionsMapperTest {

    private RegionsMapper regionsMapper;

    @BeforeEach
    public void setUp() {
        regionsMapper = new RegionsMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(regionsMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(regionsMapper.fromId(null)).isNull();
    }
}
