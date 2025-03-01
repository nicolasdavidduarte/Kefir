package org.kefir.DTOs;

import java.util.Date;

public class LoanDTO {
  private final int id;
  private final int customer;
  private final int loanType;
  private final double totalOperationAmount;
  private final Date openingDate;
  private final int currency;
  private final Date expirationDate;
  private final int totalTermDays;
  private final Date closedDate;
  private final int closedCode;
  private final Date nextInstallmentDate;
  private final int status;
  private final Date lastModificationDate;
  private final int coreUser;

  // Constructor
  public LoanDTO(
      int id,
      int customer,
      int loanType,
      double totalOperationAmount,
      Date openingDate,
      int currency,
      Date expirationDate,
      int totalTermDays,
      Date closedDate,
      int closedCode,
      Date nextInstallmentDate,
      int status,
      Date lastModificationDate,
      int coreUser) {
    this.id = id;
    this.customer = customer;
    this.loanType = loanType;
    this.totalOperationAmount = totalOperationAmount;
    this.openingDate = openingDate;
    this.currency = currency;
    this.expirationDate = expirationDate;
    this.totalTermDays = totalTermDays;
    this.closedDate = closedDate;
    this.closedCode = closedCode;
    this.nextInstallmentDate = nextInstallmentDate;
    this.status = status;
    this.lastModificationDate = lastModificationDate;
    this.coreUser = coreUser;
  }

  // Getters
  public int getId() {
    return id;
  }

  public int getCustomer() {
    return customer;
  }

  public int getLoanType() {
    return loanType;
  }

  public double getTotalOperationAmount() {
    return totalOperationAmount;
  }

  public Date getOpeningDate() {
    return openingDate;
  }

  public int getCurrency() {
    return currency;
  }

  public Date getExpirationDate() {
    return expirationDate;
  }

  public int getTotalTermDays() {
    return totalTermDays;
  }

  public Date getClosedDate() {
    return closedDate;
  }

  public int getClosedCode() {
    return closedCode;
  }

  public Date getNextInstallmentDate() {
    return nextInstallmentDate;
  }

  public int getStatus() {
    return status;
  }

  public Date getLastModificationDate() {
    return lastModificationDate;
  }

  public int getCoreUser() {
    return coreUser;
  }
}
