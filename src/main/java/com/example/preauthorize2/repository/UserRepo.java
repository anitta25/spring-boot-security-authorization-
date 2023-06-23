package com.example.preauthorize2.repository;

import com.example.preauthorize2.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo  extends JpaRepository<User,Long> {
    User findByEmail(String email);

    User findByRememberme(String value);
}
