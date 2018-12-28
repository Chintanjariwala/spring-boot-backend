package com.demo.springbootemployee.dto;

import com.demo.springbootemployee.model.RoleName;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.util.Set;

public class ManagedUserDto extends UserDto {

    @NotBlank
    @Size(min = 6, max = 40)
    private String password;

    public ManagedUserDto() {
    }

    public ManagedUserDto(Long id, String name, String username, String email, Set<RoleName> roles, Instant createdAt, Instant updatedAt, String password) {

        super(id, name, username, email, roles, createdAt, updatedAt);

        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "ManagedUserDto{" + "} " + super.toString();
    }
}
