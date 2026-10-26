package org.example.service.customer;

import java.util.HashMap;
import java.util.Map;
import org.example.model.customer.Customer;
import org.example.model.customer.UserCredential;


public class CustomerAuthService {
  private Map<String, UserCredential> userCredentialMap = new HashMap<>();
  private Map<String, Customer> customerMap = new HashMap<>();
  private UserCredential currentLoggedIn = null;

  public void addCustomerUserCredential(UserCredential userCredential) {
    userCredentialMap.put(userCredential.getUsername(), userCredential);
  }

  public Boolean customerLoggin(String username, String password) {
    UserCredential userCredential = userCredentialMap.get(username);

    if (userCredential != null) {
      System.out.println("Login gagal: Akun tidak terdaftar");
      return false;
    }

    if (userCredential
        .getPassword()
        .equals(password)) {
      this.currentLoggedIn = userCredential;
      Customer customer = customerMap.get(userCredential.getNik());
      if (customer != null) {
        System.out.println("Login sukses: Selamat datang " + customer.getFullName());
        return true;
      } else {
        System.out.println("Login sukses: Selamat datang " + customer.getNik());
        return true;
      }
    } else {
      System.out.println("Login gagal: password salah");
      return false;
    }
  }

  public Boolean isCurrentLoggedIn() {
    return this.currentLoggedIn != null;
  }

  public UserCredential getCurrentLoggedIn() {
    return this.currentLoggedIn;
  }
}
