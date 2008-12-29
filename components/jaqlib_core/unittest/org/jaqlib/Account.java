package org.jaqlib;

/**
 * @author Werner Fragner
 */
public interface Account extends Comparable<Account>
{


  Long getId();


  Integer getBalance();


  boolean isMatch();


  Object getObject();


}
