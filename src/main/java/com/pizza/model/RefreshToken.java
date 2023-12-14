package com.pizza.model;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Data
@Entity
@Table(name = "refreshtoken")
public class RefreshToken {

    @Id
    private String token;

    @OneToOne
    @JoinColumn(name = "username", referencedColumnName = "username")
    private User user;

    @Column(nullable = false)
    private Instant expiryDate;

}