package com.buoi1.webbanhang.UserRepository;

import com.buoi1.webbanhang.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}