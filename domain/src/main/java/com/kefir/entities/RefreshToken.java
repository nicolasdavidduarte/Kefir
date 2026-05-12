package com.kefir.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Setter
@Getter
@Table(name="refresh_token")
@NoArgsConstructor
@AllArgsConstructor
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 512)
    private String token;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id", nullable = false)
    private CoreUser user;

    @Column(name="expiry_date", nullable = false)
    private Instant expiryDate;

    @Column(nullable = false)
    private Boolean revoked;

    @Column(name="created_at", nullable = false)
    private final Instant createdAt = Instant.now();

    public boolean isRevoked() {
        return getRevoked();
    }
}
