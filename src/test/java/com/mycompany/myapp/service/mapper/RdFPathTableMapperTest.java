package com.mycompany.myapp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RdFPathTableMapperTest {

    private RdFPathTableMapper rdFPathTableMapper;

    @BeforeEach
    public void setUp() {
        rdFPathTableMapper = new RdFPathTableMapperImpl();
    }
}
