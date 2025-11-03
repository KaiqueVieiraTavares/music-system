package com.ms.userservice.model;

import com.ms.userservice.model.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Table(name = "users", indexes = {
        @Index(name = "idx_email", columnList = "email")
})
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(name = "user_name", unique = true)
    private String userName;
    @Column(unique = true)
    private String email;
    @Column(name = "password_hash")
    private String passwordHash;
    @Column(name = "full_name")
    private String fullName;
    private String country;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    @Enumerated(EnumType.STRING)
    private Role role;
    @PrePersist
    private void preInsert(){
        this.createdAt = LocalDateTime.now();
        this.updatedAt= LocalDateTime.now();
    }

    @PreUpdate
    private void preUpdate(){
        this.updatedAt= LocalDateTime.now();
    }
}
