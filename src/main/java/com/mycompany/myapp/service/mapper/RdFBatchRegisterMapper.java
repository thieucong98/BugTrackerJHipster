package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.RdFBatchRegister;
import com.mycompany.myapp.service.dto.RdFBatchRegisterDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link RdFBatchRegister} and its DTO {@link RdFBatchRegisterDTO}.
 */
@Mapper(componentModel = "spring")
public interface RdFBatchRegisterMapper extends EntityMapper<RdFBatchRegisterDTO, RdFBatchRegister> {}
