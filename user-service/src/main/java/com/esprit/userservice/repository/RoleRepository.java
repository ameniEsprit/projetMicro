package com.esprit.userservice.repository;

import com.esprit.userservice.entity.Role;
import com.esprit.userservice.entity.RoleType;
import com.esprit.userservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role,Long> {
    Optional<Role> findByRoleType(RoleType roleType);
}
