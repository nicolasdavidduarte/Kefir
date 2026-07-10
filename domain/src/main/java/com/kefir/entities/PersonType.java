package com.kefir.entities;

import jakarta.persistence.*;
import java.time.OffsetDateTime;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "person_type")
@NoArgsConstructor
@AllArgsConstructor
public class PersonType {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "person_type_id_seq_gen")
  @SequenceGenerator(
      name = "person_type_id_seq_gen",
      sequenceName = "person_type_id_seq",
      allocationSize = 1)
  private Integer id;

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "description", nullable = false)
  private String description;

  @Column(name = "enabled", nullable = false)
  private boolean enabled;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "created_by", nullable = false)
  private User createdBy;

  @Column(name = "created_at", nullable = false)
  private OffsetDateTime createdAt = OffsetDateTime.now();

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "updated_by", nullable = false)
  private User updatedBy;

  @Column(name = "updated_at", nullable = false)
  private OffsetDateTime updatedAt = OffsetDateTime.now();
}
