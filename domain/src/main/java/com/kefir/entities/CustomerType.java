package com.kefir.entities;

import jakarta.persistence.*;
import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "customer_type")
@NoArgsConstructor
@AllArgsConstructor
public class CustomerType {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "customer_type_id_seq_gen")
  @SequenceGenerator(
      name = "customer_type_id_seq_gen",
      sequenceName = "customer_type_id_seq",
      allocationSize = 1)
  private int id;

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "description", nullable = false)
  private String description;

  @Column(name = "enabled", nullable = false)
  private boolean enabled;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private CoreUser userId;

  @Column(name = "created_at", nullable = false)
  private OffsetDateTime createdAt;

  @Column(name = "updated_at", nullable = false)
  private OffsetDateTime updatedAt;
}
