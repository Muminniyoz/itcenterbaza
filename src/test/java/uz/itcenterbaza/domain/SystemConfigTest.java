package uz.itcenterbaza.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import uz.itcenterbaza.web.rest.TestUtil;

public class SystemConfigTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SystemConfig.class);
        SystemConfig systemConfig1 = new SystemConfig();
        systemConfig1.setId(1L);
        SystemConfig systemConfig2 = new SystemConfig();
        systemConfig2.setId(systemConfig1.getId());
        assertThat(systemConfig1).isEqualTo(systemConfig2);
        systemConfig2.setId(2L);
        assertThat(systemConfig1).isNotEqualTo(systemConfig2);
        systemConfig1.setId(null);
        assertThat(systemConfig1).isNotEqualTo(systemConfig2);
    }
}
