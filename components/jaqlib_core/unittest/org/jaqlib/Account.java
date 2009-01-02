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


  void sendInfoEmail();

}
