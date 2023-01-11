package com.rog.EShop.mapper;

import com.rog.EShop.dto.UserDto;
import com.rog.EShop.dto.UserRegisterDto;
import com.rog.EShop.entity.User;
import org.keycloak.representations.idm.UserRepresentation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

@Mapper
public interface UserMapper {
    UserDto toDTO(User user);

    @Mapping(target = "keycloakId", source = "id")
    @Mapping(target = "registerDate", source = "createdTimestamp")
    UserDto toDTO(UserRepresentation userRepresentation);

    User toEntity(UserRegisterDto userRegisterDto);

    default LocalDateTime toLocalDateTime(Long createdTimestamp){
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(createdTimestamp), ZoneOffset.UTC);
    }
    default UUID toUUID(String id){
        return UUID.fromString(id);
    }

}
