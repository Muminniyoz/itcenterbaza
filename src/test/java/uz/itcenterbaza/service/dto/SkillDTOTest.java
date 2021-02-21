package uz.itcenterbaza.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import uz.itcenterbaza.web.rest.TestUtil;

public class SkillDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SkillDTO.class);
        SkillDTO skillDTO1 = new SkillDTO();
        skillDTO1.setId(1L);
        SkillDTO skillDTO2 = new SkillDTO();
        assertThat(skillDTO1).isNotEqualTo(skillDTO2);
        skillDTO2.setId(skillDTO1.getId());
        assertThat(skillDTO1).isEqualTo(skillDTO2);
        skillDTO2.setId(2L);
        assertThat(skillDTO1).isNotEqualTo(skillDTO2);
        skillDTO1.setId(null);
        assertThat(skillDTO1).isNotEqualTo(skillDTO2);
    }
}
