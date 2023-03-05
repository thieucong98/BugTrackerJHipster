package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RdFBatchRegisterTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RdFBatchRegister.class);
        RdFBatchRegister rdFBatchRegister1 = new RdFBatchRegister();
        rdFBatchRegister1.setId(1L);
        RdFBatchRegister rdFBatchRegister2 = new RdFBatchRegister();
        rdFBatchRegister2.setId(rdFBatchRegister1.getId());
        assertThat(rdFBatchRegister1).isEqualTo(rdFBatchRegister2);
        rdFBatchRegister2.setId(2L);
        assertThat(rdFBatchRegister1).isNotEqualTo(rdFBatchRegister2);
        rdFBatchRegister1.setId(null);
        assertThat(rdFBatchRegister1).isNotEqualTo(rdFBatchRegister2);
    }
}
