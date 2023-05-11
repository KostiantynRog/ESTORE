package com.rog.EShop.services;

import com.rog.EShop.dto.UserDto;
import com.rog.EShop.dto.UserRegisterDto;
import com.rog.EShop.entity.UserActivation;
import com.rog.EShop.exceptions.BadRequestException;
import com.rog.EShop.mapper.UserMapper;
import com.rog.EShop.repository.UserActivationRepository;
import org.apache.commons.codec.digest.DigestUtils;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
public class UserService {

    private final UserMapper userMapper;
    private final KeycloakService keycloakService;
    private final UserActivationRepository userActivationRepository;
    private final JavaMailSender javaMailSender;


    public UserService(UserMapper userMapper, KeycloakMavenLibrariesServiceImpl keycloakService,
                       UserActivationRepository userActivationRepository, JavaMailSender javaMailSender) {
        this.userMapper = userMapper;
        this.keycloakService = keycloakService;
        this.userActivationRepository = userActivationRepository;
        this.javaMailSender = javaMailSender;
    }


    public UserDto findById(String id) {
        UserRepresentation userRepresentation = keycloakService.getUserById(id);
        UserDto userDto = userMapper.toDTO(userRepresentation);
        userDto.setRoles(keycloakService.getUserRoles(id));
        return userDto;
    }

    public UserDto save(UserRegisterDto userRegisterDto) {
        if (!Objects.equals(userRegisterDto.getPassword(), userRegisterDto.getConfirmPassword())) {
            throw new BadRequestException("Password does not match!");
        }

        UserRepresentation userRepresentation = keycloakService.createUser(userRegisterDto);
        UserDto userDto = userMapper.toDTO(userRepresentation);
        userDto.setRoles(keycloakService.getUserRoles(userDto.getId()));
        UserActivation userActivation = new UserActivation();
        userActivation.setUserId(keycloakService.getUserById(userDto.getId()).getId());
        userActivation.setActivationToken(DigestUtils.sha256Hex(userDto.getId() + userDto.getEmail()));
        userActivation.setTokenExpiryDate(LocalDateTime.now().plusDays(1));
        userActivationRepository.save(userActivation);
        String activationToken = userActivation.getActivationToken();
        String activationLink = "http://localhost:8080/api/user/activate/" + activationToken;
        String subject = userRegisterDto.getLastName() + ", click on the link to confirm.";
        sendEmail(userRegisterDto.getEmail(), subject, activationLink);
        return userDto;

    }

    public void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("kotrog719@gmail.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        javaMailSender.send(message);
    }


    public List<String> getUserRoles(String id) {
        return keycloakService.getUserRoles(id);
    }
}
