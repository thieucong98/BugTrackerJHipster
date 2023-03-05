package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RdFPathTableTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RdFPathTable.class);
        RdFPathTable rdFPathTable1 = new RdFPathTable();
        rdFPathTable1.setId(1L);
        RdFPathTable rdFPathTable2 = new RdFPathTable();
        rdFPathTable2.setId(rdFPathTable1.getId());
        assertThat(rdFPathTable1).isEqualTo(rdFPathTable2);
        rdFPathTable2.setId(2L);
        assertThat(rdFPathTable1).isNotEqualTo(rdFPathTable2);
        rdFPathTable1.setId(null);
        assertThat(rdFPathTable1).isNotEqualTo(rdFPathTable2);
    }
}
