package org.example.model.customer;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents customer personal identity and profile data.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "customer_records")
public class Customer {
  @Id
  @Column(name = "nik")
  private String nik;
  @Column(name = "full_name")
  private String fullName;
  @Column(name = "place_of_birth")
  private String placeOfBirth;
  @Column(name = "date_of_birth")
  private LocalDate dateOfBirth;
  @Column(name = "phone_number")
  private String phoneNumber;
  @Column(name = "gender")
  private String gender;
  @Column(name = "identity_address")
  private String identityAddress;
  @Column(name = "current_address")
  private String currentAddress;
  @Column(name = "email_address")
  private String emailAddress;
  @Column(name = "nationality")
  private String nationality;
  @Column(name = "marital_status")
  private String maritalStatus;
  @Column(name = "mother_maiden_name")
  private String motherMaidenName;
}
