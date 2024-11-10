package com.esprit.userservice.repository;

import com.esprit.userservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User,Long> {

}
