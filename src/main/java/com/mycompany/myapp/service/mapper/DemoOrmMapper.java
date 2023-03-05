package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.DemoOrm;
import com.mycompany.myapp.service.dto.DemoOrmDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link DemoOrm} and its DTO {@link DemoOrmDTO}.
 */
@Mapper(componentModel = "spring")
public interface DemoOrmMapper extends EntityMapper<DemoOrmDTO, DemoOrm> {}
