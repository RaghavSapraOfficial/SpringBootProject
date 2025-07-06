package com.flipkart.raghav.repository;

import com.flipkart.raghav.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<Users, Integer> {

    Users findByUsername(String username);
}
