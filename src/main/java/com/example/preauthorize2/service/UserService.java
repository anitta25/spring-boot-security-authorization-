package com.example.preauthorize2.service;

import com.example.preauthorize2.DTO.LoginRequestDTO;
import com.example.preauthorize2.DTO.SignupRequestDTO;
import com.example.preauthorize2.entity.User;
import com.example.preauthorize2.repository.UserRepo;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class UserService {

    private  final ModelMapper modelMapper;

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
     private  final AuthenticationManager authenticationManager;
    public UserService(ModelMapper modelMapper, UserRepo userRepo, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
        this.modelMapper = modelMapper;
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }




    public User signup(SignupRequestDTO signupRequestDTO) {
        if (Optional.ofNullable(userRepo.findByEmail(signupRequestDTO.getEmail())).isPresent())
        {
            return  null;
        }
        else
        {
            User user=new User();
            modelMapper.map(signupRequestDTO,user);
            user.setPassword(passwordEncoder.encode(signupRequestDTO.getPassword()));
            userRepo.save(user);
            return  user;
        }
    }

    public User login(LoginRequestDTO loginRequestDTO) {
        UsernamePasswordAuthenticationToken token  = new UsernamePasswordAuthenticationToken(loginRequestDTO.getEmail(),loginRequestDTO.getPassword(),null);
        try {
            Authentication authentication = authenticationManager.authenticate(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            User user = userRepo.findByEmail(loginRequestDTO.getEmail());
            log.info("login success");
            return  user;
        }
        catch (AuthenticationException exception)
        {  log.info("login fail");
            return  null;
        }
    }

    public boolean delete(Long id) {
         Optional<User> user =userRepo.findById(id);
         if (!user.isPresent())
             return  false;
         else
             userRepo.deleteById(id);
            return true;
    }

    public void createUUID(User user) {
        UUID uuid = UUID.randomUUID();
        String value=uuid.toString();
        user.setRememberme(value);
        userRepo.save(user);

    }

}
