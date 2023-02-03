package com.rog.EShop.mapper;

import com.rog.EShop.dto.UserDto;
import org.keycloak.representations.idm.UserRepresentation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

@Mapper
public interface UserMapper {
    @Mapping(target = "registerDate", source = "createdTimestamp")
    UserDto toDTO(UserRepresentation userRepresentation);

    default LocalDateTime toLocalDateTime(Long createdTimestamp){
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(createdTimestamp), ZoneOffset.UTC);
    }
    default UUID toUUID(String id){
        return UUID.fromString(id);
    }

}
