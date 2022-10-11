package com.rog.EShop.services;

import com.rog.EShop.dto.UserDto;
import com.rog.EShop.dto.UserRegisterDto;
import com.rog.EShop.entity.User;
import com.rog.EShop.exceptions.BadRequestException;
import com.rog.EShop.exceptions.ConflictException;
import com.rog.EShop.exceptions.NotFoundException;
import com.rog.EShop.mapper.UserMapper;
import com.rog.EShop.repository.RoleRepository;
import com.rog.EShop.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@Transactional(readOnly = true)
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, UserMapper userMapper, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userMapper = userMapper;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public UserDto findById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Not found"));

        return userMapper.toDTO(user);
    }
    @Transactional
    public UserDto save(UserRegisterDto userRegisterDto) {

        User user = userMapper.toEntity(userRegisterDto);
        if (userRepository.existsUserByUsername(user.getUsername())) {
            throw new ConflictException("This username already exists!");
        }

        if (!Objects.equals(userRegisterDto.getPassword(), userRegisterDto.getConfirmPassword())) {
            throw new BadRequestException("Password does not match!");
        }
        String encode = bCryptPasswordEncoder.encode(userRegisterDto.getPassword());
        user.setPassword(encode);
        user.setRegisterDate(LocalDateTime.now());
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        user.setEnabled(true);
        roleRepository.findByName("ROLE_USER").ifPresent(role -> user.setAuthorities(List.of(role)));
        User userSaved = userRepository.save(user);
        return userMapper.toDTO(userSaved);


    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("The user " + username + " does not exist"));


    }
}
