package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RdFBatchRegisterDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RdFBatchRegisterDTO.class);
        RdFBatchRegisterDTO rdFBatchRegisterDTO1 = new RdFBatchRegisterDTO();
        rdFBatchRegisterDTO1.setId(1L);
        RdFBatchRegisterDTO rdFBatchRegisterDTO2 = new RdFBatchRegisterDTO();
        assertThat(rdFBatchRegisterDTO1).isNotEqualTo(rdFBatchRegisterDTO2);
        rdFBatchRegisterDTO2.setId(rdFBatchRegisterDTO1.getId());
        assertThat(rdFBatchRegisterDTO1).isEqualTo(rdFBatchRegisterDTO2);
        rdFBatchRegisterDTO2.setId(2L);
        assertThat(rdFBatchRegisterDTO1).isNotEqualTo(rdFBatchRegisterDTO2);
        rdFBatchRegisterDTO1.setId(null);
        assertThat(rdFBatchRegisterDTO1).isNotEqualTo(rdFBatchRegisterDTO2);
    }
}
