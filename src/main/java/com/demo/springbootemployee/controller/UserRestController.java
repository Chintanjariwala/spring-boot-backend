package com.demo.springbootemployee.controller;

import com.demo.springbootemployee.dto.UserDto;
import com.demo.springbootemployee.exception.ResourceNotFoundException;
import com.demo.springbootemployee.service.UserService;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;;

@RestController
@RequestMapping("/api")
public class UserRestController {

    private static final Logger LOG = LoggerFactory.getLogger(UserRestController.class);

    @Inject
    UserService userService;

    @ApiOperation(value="Get all the users item, returns an array of users")
    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        LOG.debug("REST request to get all Users");
        final List<UserDto> users = userService.getAllManagedUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @ApiOperation(value="Get one user by username, returns a user")
    @GetMapping("/users/{username}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> getUser(@PathVariable String username) throws ResourceNotFoundException {
        LOG.debug("REST request to get User : {}", username);
        UserDto user = userService.getUserByLogin(username)
                .map(UserDto::new)
                .orElseThrow(() -> new ResourceNotFoundException("User not found for this username :: " + username));
        return new ResponseEntity<>(user, HttpStatus.OK);

    }

    @ApiOperation(value="Delete a user by username, returns deleted true or false")
    @DeleteMapping("/users/{username}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Boolean>> deleteUser(@PathVariable String username) throws ResourceNotFoundException  {
        LOG.debug("REST request to delete User : {}", username);
        userService.getUserByLogin(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found for this username :: " + username));
        userService.deleteUser(username);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
