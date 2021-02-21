package uz.itcenterbaza.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import uz.itcenterbaza.web.rest.TestUtil;

public class EventHistoryDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EventHistoryDTO.class);
        EventHistoryDTO eventHistoryDTO1 = new EventHistoryDTO();
        eventHistoryDTO1.setId(1L);
        EventHistoryDTO eventHistoryDTO2 = new EventHistoryDTO();
        assertThat(eventHistoryDTO1).isNotEqualTo(eventHistoryDTO2);
        eventHistoryDTO2.setId(eventHistoryDTO1.getId());
        assertThat(eventHistoryDTO1).isEqualTo(eventHistoryDTO2);
        eventHistoryDTO2.setId(2L);
        assertThat(eventHistoryDTO1).isNotEqualTo(eventHistoryDTO2);
        eventHistoryDTO1.setId(null);
        assertThat(eventHistoryDTO1).isNotEqualTo(eventHistoryDTO2);
    }
}
