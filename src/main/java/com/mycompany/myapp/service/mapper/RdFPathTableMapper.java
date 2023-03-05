package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.RdFPathTable;
import com.mycompany.myapp.service.dto.RdFPathTableDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link RdFPathTable} and its DTO {@link RdFPathTableDTO}.
 */
@Mapper(componentModel = "spring")
public interface RdFPathTableMapper extends EntityMapper<RdFPathTableDTO, RdFPathTable> {}
