package com.example.preauthorize2.filter;

import com.example.preauthorize2.configuration.AuthorityCollection;
import com.example.preauthorize2.entity.User;
import com.example.preauthorize2.repository.UserRepo;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.swing.text.html.Option;
import java.io.IOException;
import java.util.Optional;

@Component
@Slf4j
public class Customfilter extends OncePerRequestFilter {
    @Autowired
    AuthorityCollection collection;

    @Autowired
    UserRepo repo;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("inside filter chain");
        Cookie [] cookies = request.getCookies();
        for ( Cookie cookie: cookies)
        {
            if (cookie.getName().equals("remembermetoken"))
            { Optional<User> user=Optional.ofNullable(repo.findByRememberme(cookie.getValue()));
                if (user.isPresent())
                {
                    UsernamePasswordAuthenticationToken token = new  UsernamePasswordAuthenticationToken(user.get().getEmail(),user.get().getPassword(),collection.conversion(user.get().getAuthority()));
                    Authentication authentication = token;
                    SecurityContextHolder.getContext().setAuthentication(authentication);

                } break;

            }
        } filterChain.doFilter(request,response);
    }
}
