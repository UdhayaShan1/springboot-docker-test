package com.docker3.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.docker3.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String userName);
    User findByEmail(String email);
}
