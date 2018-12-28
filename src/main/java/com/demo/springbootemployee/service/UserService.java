package com.demo.springbootemployee.service;

import com.demo.springbootemployee.dto.UserDto;
import com.demo.springbootemployee.exception.AppException;
import com.demo.springbootemployee.model.Role;
import com.demo.springbootemployee.model.RoleName;
import com.demo.springbootemployee.model.User;
import com.demo.springbootemployee.repository.RoleRepository;
import com.demo.springbootemployee.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService {

    private final static Logger LOG = LoggerFactory.getLogger(UserService.class);

    @Inject
    UserRepository userRepository;
    @Inject
    PasswordEncoder passwordEncoder;
    @Inject
    RoleRepository roleRepository;

    public User createUser(String name, String username, String email, String password) {

        User newUser = new User();
        newUser.setName(name);
        newUser.setUsername(username);
        newUser.setEmail(email);
        newUser.setPassword(passwordEncoder.encode(password));
        newUser.setCreatedAt(Instant.now());
        Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                .orElseThrow(() -> new AppException("User Role not set."));

        newUser.setRoles(Collections.singleton(userRole));
        userRepository.save(newUser);
        LOG.debug("Created Information for User: {}", newUser);
        return newUser;
    }

    public List<UserDto> getAllManagedUsers() {
        return userRepository.findAll()
                .stream().map(UserDto::new).collect(Collectors.toList());
    }

    public Optional<User> getUserByLogin(String login) {
        return userRepository.findByUsername(login);
    }

    public void deleteUser(String login) {
        userRepository.findByUsername(login).
                ifPresent(user -> {
            userRepository.delete(user);
            LOG.debug("Deleted User: {}", user);
        });
    }
}
