package com.kefir.entities;

import com.kefir.enums.LoanStatus;
import com.kefir.exceptions.DomainErrorCode;
import com.kefir.exceptions.DomainException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "loan")
public class Loan {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "loan_id_seq_gen")
  @SequenceGenerator(name = "loan_id_seq_gen", sequenceName = "loan_id_seq", allocationSize = 1)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "customer_id", nullable = false)
  private Customer customer;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "account_id", nullable = false)
  private Account account;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "loan_type_id", nullable = false)
  private LoanType loanType;

  @Column(name = "number_of_installments", nullable = false)
  private Integer numberOfInstallments;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "currency_id", nullable = false)
  private Currency currency;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "amortization_type_id", nullable = false)
  private AmortizationType amortizationType;

  @Column(name = "annual_interest_rate", nullable = false)
  private BigDecimal annualInterestRate;

  @Column(name = "monthly_interest_rate", nullable = false)
  private BigDecimal monthlyInterestRate;

  @Column(name = "principal_amount", nullable = false)
  private BigDecimal principalAmount;

  @Column(name = "interest_amount", nullable = false)
  private BigDecimal interestAmount;

  @Column(name = "total_operation_amount", nullable = false)
  private BigDecimal totalOperationAmount;

  @Column(name = "opening_date", nullable = false)
  private OffsetDateTime openingDate;

  @Column(name = "expiration_date", nullable = false)
  private OffsetDateTime expirationDate;

  @Column(name = "remainingBalance")
  private BigDecimal remainingBalance;

  @Builder.Default
  @Column(name = "accruedInterest")
  private BigDecimal accruedInterest = BigDecimal.ZERO;

  @Column(name = "lastAccrualDate")
  private LocalDate lastAccrualDate;

  @Column(name = "external_id")
  private Long externalId;

  @Enumerated(EnumType.STRING)
  @Builder.Default
  @Column(name = "status", nullable = false)
  private LoanStatus status = LoanStatus.PENDING;

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
        + this.id
        + " / Type: "
        + this.totalOperationAmount
        + " / Installments number: "
        + this.numberOfInstallments
        + " / MIR: "
        + this.monthlyInterestRate
        + " / Expiration date: "
        + this.expirationDate
        + " / Status: "
        + this.status;
  }

  public void approve(User approvedBy) {
    if (this.getStatus() != LoanStatus.PENDING)
      throw new DomainException(DomainErrorCode.LOAN_NOT_VALID);

    this.status = LoanStatus.ACTIVE;
    this.updatedBy = approvedBy;
    this.updatedAt = OffsetDateTime.now();
  }

  public void chargeOff(User approvedBy) {
    if (this.getStatus() != LoanStatus.ACTIVE)
      throw new DomainException(DomainErrorCode.LOAN_NOT_VALID);

    this.setStatus(LoanStatus.CHARGE_OFF);
    this.setUpdatedBy(approvedBy);
    this.setUpdatedAt(OffsetDateTime.now());
  }

  public void close(User closedBy) {
    this.status = LoanStatus.CLOSED;
    this.updatedBy = closedBy;
    this.updatedAt = OffsetDateTime.now();
  }

  public static Loan create(
      Customer customer,
      Account account,
      LoanType loanType,
      AmortizationType amortizationType,
      BigDecimal principalAmount,
      Currency currency,
      int numberOfInstallments,
      Long externalId,
      User user) {

    if (principalAmount == null || principalAmount.compareTo(BigDecimal.ZERO) <= 0) {
      throw new DomainException(DomainErrorCode.INVALID_AMOUNT);
    }

    if (numberOfInstallments <= 0) {
      throw new DomainException(DomainErrorCode.INVALID_INSTALLMENTS);
    }

    OffsetDateTime now = OffsetDateTime.now();

    BigDecimal annualRate = loanType.getAnnualInterestRate();

    BigDecimal monthlyRate = annualRate.divide(BigDecimal.valueOf(12), 10, RoundingMode.HALF_UP);

    return Loan.builder()
        .customer(customer)
        .account(account)
        .loanType(loanType)
        .numberOfInstallments(numberOfInstallments)
        .currency(currency)
        .amortizationType(amortizationType)
        .annualInterestRate(annualRate)
        .monthlyInterestRate(monthlyRate)
        .principalAmount(principalAmount)
        .interestAmount(BigDecimal.ZERO)
        .totalOperationAmount(BigDecimal.ZERO)
        .openingDate(now)
        .expirationDate(now.plusMonths(numberOfInstallments))
        .accruedInterest(BigDecimal.ZERO)
        .remainingBalance(principalAmount)
        .externalId(externalId)
        .status(LoanStatus.PENDING)
        .createdBy(user)
        .createdAt(now)
        .updatedBy(user)
        .updatedAt(now)
        .build();
  }

  public void initializeFinancialAmounts(List<LoanInstallment> loanInstallments) {
    this.totalOperationAmount =
        loanInstallments.stream()
            .map(LoanInstallment::getTotalAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

    this.interestAmount =
        loanInstallments.stream()
            .map(LoanInstallment::getInterestAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
  }

  public boolean isActive() {
    return this.status == LoanStatus.ACTIVE;
  }

  public void updateRemainingBalance(User updatedBy, BigDecimal capitalAmount) {
    if (this.status != LoanStatus.ACTIVE) {
      throw new DomainException(DomainErrorCode.LOAN_NOT_VALID);
    }

    if (interestAmount.signum() < 0) {
      throw new DomainException(DomainErrorCode.INVALID_AMOUNT);
    }

    this.remainingBalance = remainingBalance.subtract(capitalAmount);
    this.updatedBy = updatedBy;
    this.updatedAt = OffsetDateTime.now();
  }

  public void accrueInterest(User updatedBy, BigDecimal interestAmount) {
    if (this.status != LoanStatus.ACTIVE) {
      throw new DomainException(DomainErrorCode.LOAN_NOT_VALID);
    }

    LocalDate today = LocalDate.now();

    if (Objects.equals(this.lastAccrualDate, today)) {
      throw new DomainException(DomainErrorCode.LOAN_ALREADY_ACCRUED);
    }

    if (interestAmount.signum() < 0) {
      throw new DomainException(DomainErrorCode.INVALID_AMOUNT);
    }

    this.accruedInterest = accruedInterest.add(interestAmount);
    this.lastAccrualDate = LocalDate.now();
    this.updatedBy = updatedBy;
    this.updatedAt = OffsetDateTime.now();
  }
}
