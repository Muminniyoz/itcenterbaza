package uz.itcenterbaza.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class EventHistoryMapperTest {

    private EventHistoryMapper eventHistoryMapper;

    @BeforeEach
    public void setUp() {
        eventHistoryMapper = new EventHistoryMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(eventHistoryMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(eventHistoryMapper.fromId(null)).isNull();
    }
}
