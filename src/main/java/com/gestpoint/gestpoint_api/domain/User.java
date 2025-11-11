package com.gestpoint.gestpoint_api.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "users")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String passwordHash;
    private String phoneArea;
    private String phoneNumber;
    private String email;
    private String googleToken;
    private String role;
    private Long tenantId;
}
