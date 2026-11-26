package com.kefir.entities;

import com.kefir.enums.CustomerStatus;
import com.kefir.exceptions.DomainErrorCode;
import com.kefir.exceptions.DomainException;
import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.function.Consumer;
import lombok.*;

@Getter
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

  public static Customer create(
      String name1,
      String name2,
      String name3,
      String lastname1,
      String lastname2,
      String lastname3,
      PersonType personType,
      DocumentType documentType,
      String documentNumber,
      CustomerType customerType,
      String email,
      User user) {
    return Customer.builder()
        .name1(name1)
        .name2(name2)
        .name3(name3)
        .lastname1(lastname1)
        .lastname2(lastname2)
        .lastname3(lastname3)
        .fullname(generateFullname(name1, name2, name3, lastname1, lastname2, lastname3))
        .personType(personType)
        .documentType(documentType)
        .documentNumber(documentNumber)
        .customerType(customerType)
        .email(email)
        .createdBy(user)
        .createdAt(OffsetDateTime.now())
        .updatedBy(user)
        .updatedAt(OffsetDateTime.now())
        .build();
  }

  public void update(
      String name1,
      String name2,
      String name3,
      String lastname1,
      String lastname2,
      String lastname3,
      PersonType personType,
      DocumentType documentType,
      String documentNumber,
      CustomerType customerType,
      String email,
      User user) {
    if (status != CustomerStatus.ACTIVE)
      throw new DomainException(DomainErrorCode.CUSTOMER_NOT_VALID);

    updateIfChanged(name1, this.name1, (n1) -> this.name1 = n1.toString());
    updateIfChanged(name2, this.name2, (n2) -> this.name2 = n2.toString());
    updateIfChanged(name3, this.name3, (n3) -> this.name3 = n3.toString());

    updateIfChanged(lastname1, this.getLastname1(), (ln1) -> this.lastname1 = ln1.toString());
    updateIfChanged(lastname2, this.getLastname2(), (ln2) -> this.lastname2 = ln2.toString());
    updateIfChanged(lastname3, this.getLastname3(), (ln3) -> this.lastname3 = ln3.toString());

    updateIfChanged(personType, this.personType, (pt -> this.personType = (PersonType) pt));

    updateIfChanged(customerType, this.customerType, (ct -> this.customerType = (CustomerType) ct));

    updateIfChanged(documentType, this.documentType, (dt -> this.documentType = (DocumentType) dt));

    updateIfChanged(documentNumber, this.documentNumber, (dn -> this.documentNumber = (String) dn));

    updateIfChanged(email, this.email, (e -> this.email = (String) e));

    String fullname =
        Customer.generateFullname(
            this.name1, this.name2, this.name3, this.lastname1, this.lastname2, this.lastname3);
    updateIfChanged(fullname, this.fullname, (fn) -> this.fullname = fn.toString());

    this.updatedBy = user;
    this.updatedAt = OffsetDateTime.now();
  }

  public void activate(User user) {
    if ((status != CustomerStatus.PENDING) && (status != CustomerStatus.DEACTIVATED))
      throw new DomainException(DomainErrorCode.CUSTOMER_NOT_VALID);

    this.status = CustomerStatus.ACTIVE;
    this.updatedBy = user;
    this.updatedAt = OffsetDateTime.now();
  }

  public void deactivate(User user) {
    if (status != CustomerStatus.ACTIVE)
      throw new DomainException(DomainErrorCode.CUSTOMER_NOT_VALID);

    this.status = CustomerStatus.DEACTIVATED;
    this.updatedBy = user;
    this.updatedAt = OffsetDateTime.now();
  }

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

  public boolean isActive() {
    return status == CustomerStatus.ACTIVE;
  }

  public static String generateFullname(
      String name1,
      String name2,
      String name3,
      String lastname1,
      String lastname2,
      String lastname3) {
    StringBuilder fullname = new StringBuilder();

    fullname.append(name1);

    if ((name2 != null) && (!name2.isBlank())) {
      fullname.append(' ').append(name2);
    }

    if ((name3 != null) && (!name3.isBlank())) {
      fullname.append(' ').append(name3);
    }

    fullname.append(' ').append(lastname1);

    if ((lastname2 != null) && (!lastname2.isBlank())) {
      fullname.append(' ').append(lastname2);
    }

    if ((lastname3 != null) && (!lastname3.isBlank())) {
      fullname.append(' ').append(lastname3);
    }

    return fullname.toString();
  }

  private void updateIfChanged(Object newValue, Object currentValue, Consumer<Object> setter) {
    if (newValue != null && !newValue.equals(currentValue)) {
      setter.accept(newValue);
    }
  }
}
