package com.example.preauthorize2.configuration;

import com.example.preauthorize2.filter.Customfilter;
import com.example.preauthorize2.service.UserDetService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    UserDetService userDetService;
    @Autowired
    Customfilter customfilter;
    @Bean
    public ModelMapper modelMapper() throws Exception
    {
        return  new ModelMapper();
    }
    @Bean
    public PasswordEncoder passwordEncoder () throws  Exception
    {
        return  new BCryptPasswordEncoder();
    }
    @Bean
    public SecurityFilterChain securityFilterChain (HttpSecurity httpSecurity) throws  Exception
    {
         httpSecurity                            .csrf(AbstractHttpConfigurer ::disable);
         httpSecurity                            .authorizeHttpRequests((req)->req
                                                .requestMatchers("/login","/signup","/logout").permitAll()
                 .requestMatchers("/deleteuser/{id}").hasAuthority("ROLE_ADMIN")
                 .requestMatchers("/adminpage").hasAuthority("ROLE_ADMIN")
                 .anyRequest().authenticated());
         httpSecurity   .logout().disable();
         httpSecurity   .addFilterBefore(customfilter, BasicAuthenticationFilter.class);
       return httpSecurity.build();
    }
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() throws  Exception
    {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(userDetService);
        return provider;
    }
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity httpSecurity) throws  Exception
    {
        AuthenticationManagerBuilder authenticationManagerBuilder= httpSecurity.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.authenticationProvider(daoAuthenticationProvider());
        return  authenticationManagerBuilder.build();
    }
}
