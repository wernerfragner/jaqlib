package org.jaqlib;

import junit.framework.TestCase;

public class AccountAssert
{

  public static void assertMaierAccount(Account account)
  {
    assertEquals(AccountSetup.MAIER_ACCOUNT, account);
  }


  public static void assertHuberAccount(Account account)
  {
    assertEquals(AccountSetup.HUBER_ACCOUNT, account);
  }


  public static void assertEquals(Account expected, Account given)
  {
    TestCase.assertEquals(expected.getLastName(), given.getLastName());
    TestCase.assertEquals(expected.getFirstName(), given.getFirstName());
    TestCase.assertEquals(expected.getBalance(), given.getBalance());
    TestCase.assertEquals(expected.getCreditRating(), given.getCreditRating());
  }

}
