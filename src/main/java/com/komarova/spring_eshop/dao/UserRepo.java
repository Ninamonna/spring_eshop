package com.komarova.spring_eshop.dao;

import com.komarova.spring_eshop.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, Long> {
    User findFirstByName(String name);
}
