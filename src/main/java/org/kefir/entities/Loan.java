package org.kefir.entities;

import jakarta.persistence.*;

import java.util.Date;

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

    // Constructors
    public Loan() {}

    public Loan(int id, int customer, int loanType, double totalOperationAmount, Date openingDate, int currency, Date expirationDate, int totalTermDays, Date closedDate, int closedCode, Date nextInstallmentDate, int status, Date lastModificationDate, int coreUser) {
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCustomer() {
        return customer;
    }

    public void setCustomer(int customer) {
        this.customer = customer;
    }

    public int getLoanType() {
        return loanType;
    }

    public void setLoanType(int loanType) {
        this.loanType = loanType;
    }

    public double getTotalOperationAmount() {
        return totalOperationAmount;
    }

    public void setTotalOperationAmount(double totalOperationAmount) {
        this.totalOperationAmount = totalOperationAmount;
    }

    public Date getOpeningDate() {
        return openingDate;
    }

    public void setOpeningDate(Date openingDate) {
        this.openingDate = openingDate;
    }

    public int getCurrency() {
        return currency;
    }

    public void setCurrency(int currency) {
        this.currency = currency;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public int getTotalTermDays() {
        return totalTermDays;
    }

    public void setTotalTermDays(int totalTermDays) {
        this.totalTermDays = totalTermDays;
    }

    public Date getClosedDate() {
        return closedDate;
    }

    public void setClosedDate(Date closedDate) {
        this.closedDate = closedDate;
    }

    public int getClosedCode() {
        return closedCode;
    }

    public void setClosedCode(int closedCode) {
        this.closedCode = closedCode;
    }

    public Date getNextInstallmentDate() {
        return nextInstallmentDate;
    }

    public void setNextInstallmentDate(Date nextInstallmentDate) {
        this.nextInstallmentDate = nextInstallmentDate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Date getLastModificationDate() {
        return lastModificationDate;
    }

    public void setLastModificationDate(Date lastModificationDate) {
        this.lastModificationDate = lastModificationDate;
    }

    public int getCoreUser() {
        return coreUser;
    }

    public void setCoreUser(int coreUser) {
        this.coreUser = coreUser;
    }
}
