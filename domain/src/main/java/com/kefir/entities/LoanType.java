package com.kefir.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "loan_type")
public class LoanType {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "loan_type_seq_gen")
  @SequenceGenerator(
          name = "loan_type_seq_gen",
          sequenceName = "loan_type_id_seq",
          allocationSize = 1
  )
  private int id;

  private String name;
  private String description;

}
