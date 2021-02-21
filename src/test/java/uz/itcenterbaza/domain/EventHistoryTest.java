package uz.itcenterbaza.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import uz.itcenterbaza.web.rest.TestUtil;

public class EventHistoryTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EventHistory.class);
        EventHistory eventHistory1 = new EventHistory();
        eventHistory1.setId(1L);
        EventHistory eventHistory2 = new EventHistory();
        eventHistory2.setId(eventHistory1.getId());
        assertThat(eventHistory1).isEqualTo(eventHistory2);
        eventHistory2.setId(2L);
        assertThat(eventHistory1).isNotEqualTo(eventHistory2);
        eventHistory1.setId(null);
        assertThat(eventHistory1).isNotEqualTo(eventHistory2);
    }
}
