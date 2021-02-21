package uz.itcenterbaza.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import uz.itcenterbaza.web.rest.TestUtil;

public class RegionsDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RegionsDTO.class);
        RegionsDTO regionsDTO1 = new RegionsDTO();
        regionsDTO1.setId(1L);
        RegionsDTO regionsDTO2 = new RegionsDTO();
        assertThat(regionsDTO1).isNotEqualTo(regionsDTO2);
        regionsDTO2.setId(regionsDTO1.getId());
        assertThat(regionsDTO1).isEqualTo(regionsDTO2);
        regionsDTO2.setId(2L);
        assertThat(regionsDTO1).isNotEqualTo(regionsDTO2);
        regionsDTO1.setId(null);
        assertThat(regionsDTO1).isNotEqualTo(regionsDTO2);
    }
}
