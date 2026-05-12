package com.kefir.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "customer")
public class Customer {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "customer_seq_gen")
  @SequenceGenerator(
      name = "customer_seq_gen",
      sequenceName = "customer_id_seq",
      allocationSize = 1)
  private Long id;

  @Column(nullable = false)
  private String name1;

  private String name2;
  private String name3;

  @Column(nullable = false)
  private String lastname1;

  private String lastname2;
  private String lastname3;
  private String fullname;

  @Column(nullable = false)
  private Integer personType;

  @Column(nullable = false)
  private Integer documentType;

  @Column(nullable = false)
  private Integer documentNumber;

  @Column(nullable = false)
  private Integer customerType;

  @Column(nullable = false)
  private Integer status;

  @Column(nullable = false)
  private LocalDateTime lastModificationDate;

  @Override
  public String toString() {
    return "{Customer Id: " + id + " / name: " + fullname + "}";
  }
}
