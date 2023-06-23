package com.example.preauthorize2.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table (name = "sampleusers")
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
            Long id;
    String email,password, authority,rememberme;
}
