package kpfu.crm.mapper;

import kpfu.crm.model.user.AppUser;
import kpfu.crm.model.user.dto.CreateUserDTO;
import kpfu.crm.model.user.dto.UserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper
public interface UserMapper {

    UserDTO toDto(AppUser user);

    @Mapping(target = "password", ignore = true)
    void toEntity(@MappingTarget AppUser test, CreateUserDTO dto);
}
