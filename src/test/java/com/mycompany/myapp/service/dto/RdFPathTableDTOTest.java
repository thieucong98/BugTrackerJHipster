package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RdFPathTableDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RdFPathTableDTO.class);
        RdFPathTableDTO rdFPathTableDTO1 = new RdFPathTableDTO();
        rdFPathTableDTO1.setId(1L);
        RdFPathTableDTO rdFPathTableDTO2 = new RdFPathTableDTO();
        assertThat(rdFPathTableDTO1).isNotEqualTo(rdFPathTableDTO2);
        rdFPathTableDTO2.setId(rdFPathTableDTO1.getId());
        assertThat(rdFPathTableDTO1).isEqualTo(rdFPathTableDTO2);
        rdFPathTableDTO2.setId(2L);
        assertThat(rdFPathTableDTO1).isNotEqualTo(rdFPathTableDTO2);
        rdFPathTableDTO1.setId(null);
        assertThat(rdFPathTableDTO1).isNotEqualTo(rdFPathTableDTO2);
    }
}
