package com.kefir.entities;

import jakarta.persistence.*;
import java.time.OffsetDateTime;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "currency")
@NoArgsConstructor
@AllArgsConstructor
public class Currency {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "currency_id_seq_gen")
  @SequenceGenerator(
      name = "currency_id_seq_gen",
      sequenceName = "currency_id_seq",
      allocationSize = 1)
  private Integer id;

  @Column(name = "iso_code", nullable = false)
  private String isoCode;

  @Column(name = "description", nullable = false)
  private String description;

  @Column(name = "enabled", nullable = false)
  private boolean enabled;

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

  @Override
  public String toString() {
    return "Id: "
        + id
        + " / ISO code: "
        + isoCode
        + " / description: "
        + description
        + " / enabled: "
        + enabled;
  }
}
