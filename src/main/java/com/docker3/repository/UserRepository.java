package com.docker3.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.docker3.model.Users;

public interface UserRepository extends JpaRepository<Users, Long> {
    Users findByUsername(String userName);
    Users findByEmail(String email);
}
