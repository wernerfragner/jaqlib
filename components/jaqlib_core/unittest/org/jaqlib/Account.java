package org.jaqlib;

/**
 * @author Werner Fragner
 */
public interface Account extends Comparable<Account>
{

  Long getId();


  Double getBalance();


  String getLastName();


  String getFirstName();


  CreditRating getCreditRating();


  void setDepartmentObject(Department department);


  void setDepartment(String department);


  Department getDepartmentObj();


  void sendInfoEmail();


  void setActive(boolean active);


  boolean isActive();


}
