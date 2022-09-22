package com.rog.EShop.services;

import com.rog.EShop.dto.UserDto;
import com.rog.EShop.dto.UserRegisterDto;
import com.rog.EShop.entity.User;
import com.rog.EShop.exceptions.BadRequestException;
import com.rog.EShop.exceptions.ConflictException;
import com.rog.EShop.exceptions.NotFoundException;
import com.rog.EShop.mapper.UserMapper;
import com.rog.EShop.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserService(UserRepository userRepository, UserMapper userMapper, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public UserDto findById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Not found"));

        return userMapper.toDTO(user);
    }

    public UserDto save(UserRegisterDto userRegisterDto) {

        User user = userMapper.toEntity(userRegisterDto);
        if (userRepository.existsUserByUsername(user.getUsername())) {
            throw new ConflictException("This username already exists!");
        }

        if (!(userRegisterDto.getPassword().equals(userRegisterDto.getConfirmPassword()))) {
            throw new BadRequestException("Password does not match!");
        }
        String encode = bCryptPasswordEncoder.encode(userRegisterDto.getPassword());
        User userSaved = userRepository.save(user);
        return userMapper.toDTO(userSaved);


    }
}
