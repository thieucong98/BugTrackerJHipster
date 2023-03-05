package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DemoOrmDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DemoOrmDTO.class);
        DemoOrmDTO demoOrmDTO1 = new DemoOrmDTO();
        demoOrmDTO1.setId(1L);
        DemoOrmDTO demoOrmDTO2 = new DemoOrmDTO();
        assertThat(demoOrmDTO1).isNotEqualTo(demoOrmDTO2);
        demoOrmDTO2.setId(demoOrmDTO1.getId());
        assertThat(demoOrmDTO1).isEqualTo(demoOrmDTO2);
        demoOrmDTO2.setId(2L);
        assertThat(demoOrmDTO1).isNotEqualTo(demoOrmDTO2);
        demoOrmDTO1.setId(null);
        assertThat(demoOrmDTO1).isNotEqualTo(demoOrmDTO2);
    }
}
