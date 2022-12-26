package com.komarova.spring_eshop.service;

import com.komarova.spring_eshop.dao.UserRepo;
import com.komarova.spring_eshop.domain.Role;
import com.komarova.spring_eshop.domain.User;
import com.komarova.spring_eshop.dto.UserDTO;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService{
    private final UserRepo userRepo;
    private  final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepo userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public boolean save(UserDTO userDTO) {
        if (!Objects.equals(userDTO.getPassword(), userDTO.getMatchingPassword())) {
            throw new RuntimeException("Passwords is not equals");
        }
        User user = User.builder()
                .name(userDTO.getUsername())
                .password(userDTO.getPassword())
                .email(userDTO.getEmail())
                .role(Role.CLIENT)
                .build();
        userRepo.save(user);
        return true;
    }

    @Override
    public void save(User user) {
        userRepo.save(user);

    }

    @Override
    public List<UserDTO> getAll() {
        return userRepo.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public User findByName(String name) {
        return userRepo.findFirstByName(name);
    }

    @Override
    @Transactional
    public void updateProfile(UserDTO dto) {
        User savedUser = userRepo.findFirstByName(dto.getUsername());
        if (savedUser == null) {
            throw new RuntimeException("you are not authorization " + dto.getUsername());
        }
        boolean changed = false;
        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            savedUser.setPassword(passwordEncoder.encode(dto.getPassword()));
            changed = true;
        }
        if (!Objects.equals(dto.getEmail(), savedUser.getEmail())) {
            savedUser.setEmail(dto.getEmail());
            changed = true;
        }
        if (changed) {
            userRepo.save(savedUser);
        }

    }

    private UserDTO toDto(User user) {
        return UserDTO.builder()
                .username(user.getName())
                .email(user.getEmail())
                .build();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findFirstByName(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with name: " + username);
        }

        List<GrantedAuthority> roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority(user.getRole().name()));
        return new org.springframework.security.core.userdetails.User(
                user.getName(),
                user.getPassword(),
                roles
        );
    }
}
