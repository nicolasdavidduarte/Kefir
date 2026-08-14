package com.kefir.entities;

import jakarta.persistence.*;
import java.time.Instant;
import java.time.OffsetDateTime;
import lombok.*;

@Entity
@Setter
@Getter
@Table(name = "refresh_token")
@NoArgsConstructor
@AllArgsConstructor
public class RefreshToken {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true, length = 512)
  private String token;

  @Column(name = "expiry_date", nullable = false)
  private Instant expiryDate;

  @Column(nullable = false)
  private Boolean revoked;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "created_by", nullable = false)
  private User createdBy;

  @Column(name = "created_at", nullable = false)
  private OffsetDateTime createdAt;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "updated_by", nullable = false)
  private User updatedBy;

  @Column(name = "updated_at", nullable = false)
  private OffsetDateTime updatedAt;

  public boolean isRevoked() {
    return getRevoked();
  }

  @Override
  public String toString() {
    return "Id: " + id + " / Expiry date: " + expiryDate + " / revoked: " + revoked;
  }
}
