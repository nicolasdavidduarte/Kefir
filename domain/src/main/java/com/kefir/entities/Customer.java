package com.kefir.entities;

import com.kefir.enums.CustomerStatus;
import jakarta.persistence.*;
import java.time.OffsetDateTime;
import lombok.*;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "customer")
public class Customer {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "customer_id_seq_gen")
  @SequenceGenerator(
      name = "customer_id_seq_gen",
      sequenceName = "customer_id_seq",
      allocationSize = 1)
  private Long id;

  @Column(name = "name1", nullable = false)
  private String name1;

  @Column(name = "name2")
  private String name2;

  @Column(name = "name3")
  private String name3;

  @Column(name = "lastname1", nullable = false)
  private String lastname1;

  @Column(name = "lastname2")
  private String lastname2;

  @Column(name = "lastname3")
  private String lastname3;

  @Column(name = "fullname", nullable = false)
  private String fullname;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "person_type_id", nullable = false)
  private PersonType personType;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "document_type_id", nullable = false)
  private DocumentType documentType;

  @Column(name = "document_number", nullable = false)
  private String documentNumber;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "customer_type_id", nullable = false)
  private CustomerType customerType;

  @Column(name = "email")
  private String email;

  @Enumerated(EnumType.STRING)
  @Builder.Default
  @Column(name = "status", nullable = false)
  private CustomerStatus status = CustomerStatus.PENDING;

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
    return "{Id: "
        + id
        + " / Fullname: "
        + fullname
        + " / Document number: "
        + documentNumber
        + " / E-mail : "
        + email
        + " / Status: "
        + status
        + "}";
  }
}
