package com.demo.springbootemployee.repository;

import java.util.Optional;

import com.demo.springbootemployee.model.Role;
import com.demo.springbootemployee.model.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleName roleName);
}