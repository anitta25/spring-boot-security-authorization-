package com.example.preauthorize2.controller;

import com.example.preauthorize2.DTO.LoginRequestDTO;
import com.example.preauthorize2.DTO.SignupRequestDTO;
import com.example.preauthorize2.entity.User;
import com.example.preauthorize2.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.w3c.dom.html.HTMLTableCaptionElement;

import java.util.HashMap;
import java.util.Map;

@RestController
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequestDTO signupRequestDTO)
    {
        User user=userService.signup(signupRequestDTO);
        if (user==null)
            return  ResponseEntity.status(HttpStatus.CONFLICT).body("username already exists");
        else
        {
            Map<String,Object> response= new HashMap<>();
            response.put("message","account created successfully");
            response.put("body",user);
            return  ResponseEntity.status(HttpStatus.OK).body(response);
        }

    }
     @PostMapping("/login")
    public ResponseEntity<String>  login(@RequestBody LoginRequestDTO loginRequestDTO, HttpServletResponse response)
     {
         User user=userService.login(loginRequestDTO);
         if(user!=null)
     {

             userService.createUUID(user);
             Cookie cookie = new Cookie("remembermetoken" , user.getRememberme());
             cookie.setMaxAge(10*24*60*60);
             response.addCookie(cookie);
         return ResponseEntity.status(HttpStatus.OK).body("login success");
         }
         else
             return  ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("login fail");
     }
     @DeleteMapping("/deleteuser/{id}")
    public  ResponseEntity<String> deleteuser(@PathVariable Long id)
     {
            if(userService.delete(id))
            {
                return  ResponseEntity.status(HttpStatus.OK).body("deleted");

            }
            else
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("user not found");
     }
     @GetMapping("/adminpage")
    public  String adminpage()
     {
         return  "admin";
     }
     @PostMapping("/logout")
    public  String logout(HttpServletRequest request,HttpServletResponse httpServletResponse)
     {
         Cookie [] cookies=request.getCookies();
         if(cookies!=null)
         {
             for(Cookie cookie: cookies)
             {
                 if(cookie.getName().equals("remembermetoken"))
                 {
                     cookie.setMaxAge(0);
                     httpServletResponse.addCookie(cookie);
                     break;
                 }
             }
         } return  "logout successful";
     }
}
