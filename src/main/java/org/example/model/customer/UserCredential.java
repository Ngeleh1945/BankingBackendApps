package org.example.model.customer;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents customer, user authentication credentials for mobile and internet banking login.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_credential")
public class UserCredential {
  @Id
  @Column(name = "account_id")
  private String accountId;
  @Column(name = "username")
  private String username;
  @Column(name = "password")
  private String password;
  @Column(name = "nik")
  private String nik;
  @Column(name = "biometric_data_photos")
  private String biometricDataPhotos;
  @Column(name = "biometric_data")
  private String biometricData;
}
