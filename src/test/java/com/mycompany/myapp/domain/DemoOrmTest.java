package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DemoOrmTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DemoOrm.class);
        DemoOrm demoOrm1 = new DemoOrm();
        demoOrm1.setId(1L);
        DemoOrm demoOrm2 = new DemoOrm();
        demoOrm2.setId(demoOrm1.getId());
        assertThat(demoOrm1).isEqualTo(demoOrm2);
        demoOrm2.setId(2L);
        assertThat(demoOrm1).isNotEqualTo(demoOrm2);
        demoOrm1.setId(null);
        assertThat(demoOrm1).isNotEqualTo(demoOrm2);
    }
}
