package com.rog.EShop.mapper;

import com.rog.EShop.dto.UserDto;
import org.keycloak.representations.idm.UserRepresentation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.*;

@Mapper
public interface UserMapper {
    @Mapping(target = "registerDate", source = "createdTimestamp")
    @Mapping(target = "roles", source = "realmRoles")
    UserDto toDTO(UserRepresentation userRepresentation);

    default LocalDateTime toLocalDateTime(Long createdTimestamp) {
        if (createdTimestamp == null) {
            return null;
        }
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(createdTimestamp), ZoneOffset.UTC);
    }

}
