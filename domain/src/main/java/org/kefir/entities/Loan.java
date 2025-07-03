package org.kefir.entities;

import jakarta.persistence.*;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "loan")
public class Loan {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;
  private int customer;
  private int loanType;
  private double totalOperationAmount;
  private Date openingDate;
  private int currency;
  private Date expirationDate;
  private int totalTermDays;
  private Date closedDate;
  private int closedCode;
  private Date nextInstallmentDate;
  private int status;
  private Date lastModificationDate;
  private int coreUser;

  // Constructor vacío necesario para JPA
  public Loan() {}

  // Getters y setters...
  // (sin constructor con LoanDTO)
}
