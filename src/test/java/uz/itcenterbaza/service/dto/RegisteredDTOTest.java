package uz.itcenterbaza.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import uz.itcenterbaza.web.rest.TestUtil;

public class RegisteredDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RegisteredDTO.class);
        RegisteredDTO registeredDTO1 = new RegisteredDTO();
        registeredDTO1.setId(1L);
        RegisteredDTO registeredDTO2 = new RegisteredDTO();
        assertThat(registeredDTO1).isNotEqualTo(registeredDTO2);
        registeredDTO2.setId(registeredDTO1.getId());
        assertThat(registeredDTO1).isEqualTo(registeredDTO2);
        registeredDTO2.setId(2L);
        assertThat(registeredDTO1).isNotEqualTo(registeredDTO2);
        registeredDTO1.setId(null);
        assertThat(registeredDTO1).isNotEqualTo(registeredDTO2);
    }
}
