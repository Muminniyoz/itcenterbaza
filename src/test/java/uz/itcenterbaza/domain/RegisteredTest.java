package uz.itcenterbaza.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import uz.itcenterbaza.web.rest.TestUtil;

public class RegisteredTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Registered.class);
        Registered registered1 = new Registered();
        registered1.setId(1L);
        Registered registered2 = new Registered();
        registered2.setId(registered1.getId());
        assertThat(registered1).isEqualTo(registered2);
        registered2.setId(2L);
        assertThat(registered1).isNotEqualTo(registered2);
        registered1.setId(null);
        assertThat(registered1).isNotEqualTo(registered2);
    }
}
