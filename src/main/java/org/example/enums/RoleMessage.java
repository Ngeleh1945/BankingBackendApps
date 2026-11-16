package org.example.enums;

public enum RoleMessage {
  DESCRIPTIONALREADYEXIST("Failed to add: Role description already exists"),
  NOT_FOUND("Role with description %s is not found"),
  ADD_SUCCESSFULL("Success to add: Role account added successfully"),
  RETRIEVE_SUCCESS("Roles successfully retrieved");

  private final String desc;

  RoleMessage(String desc) {
    this.desc = desc;
  }

  public String getDesc() {
    return desc;
  }
}
