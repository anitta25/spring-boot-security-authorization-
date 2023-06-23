package com.example.preauthorize2.service;

import com.example.preauthorize2.configuration.AuthorityCollection;
import com.example.preauthorize2.entity.User;
import com.example.preauthorize2.repository.UserRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class
UserDetService implements UserDetailsService {
    @Autowired
    AuthorityCollection authorityCollection;

    @Autowired
    UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user= Optional.ofNullable(userRepo.findByEmail(email))
                .orElseThrow(()->new UsernameNotFoundException("username not found"));
        log.info("inside loaduserbyname");
        org.springframework.security.core.userdetails.User user1=new org.springframework.security.core.userdetails.User(user.getEmail(),user.getPassword(), authorityCollection.conversion(user.getAuthority()));
      log.info("user1 created");
       return  user1;
    }
}

