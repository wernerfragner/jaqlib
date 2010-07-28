package org.jaqlib;

import java.util.List;

import junit.framework.TestCase;

public class AccountAssert
{

  public static void assertMaierAccount(Account account)
  {
    TestCase.assertNotNull(account);
    assertEquals(AccountSetup.MAIER_ACCOUNT, account);
  }


  public static void assertHuberAccount(Account account)
  {
    TestCase.assertNotNull(account);
    assertEquals(AccountSetup.HUBER_ACCOUNT, account);
  }


  public static void assertEquals(Account expected, Account given)
  {
    TestCase.assertEquals(expected.getLastName(), given.getLastName());
    TestCase.assertEquals(expected.getFirstName(), given.getFirstName());
    TestCase.assertEquals(expected.getBalance(), given.getBalance());
    TestCase.assertEquals(expected.getCreditRating(), given.getCreditRating());
  }


  public static void assertMaierAccount(List<AccountImpl> accounts)
  {
    TestCase.assertNotNull(accounts);
    TestCase.assertEquals(1, accounts.size());
    AccountAssert.assertMaierAccount(accounts.get(0));
  }


  public static void assertHuberAccount(List<AccountImpl> accounts)
  {
    TestCase.assertNotNull(accounts);
    TestCase.assertEquals(1, accounts.size());
    AccountAssert.assertHuberAccount(accounts.get(0));
  }


  public static void assertMaierAccountWithTransactions(
      List<AccountImpl> accounts)
  {
    assertMaierAccount(accounts);
    assertMaierTransactions(accounts);
  }


  public static void assertMaierTransactions(List<AccountImpl> accounts)
  {
    List<? extends Transaction> transactions = accounts.get(0)
        .getTransactions();
    TestCase.assertEquals(2, transactions.size());
    JaqlibAssert.assertEqualLists(AccountSetup.MAIER_ACCOUNT.getTransactions(),
        transactions);
  }


  public static void assertHuberAccountWithTransactions(
      List<AccountImpl> accounts)
  {
    assertHuberAccount(accounts);
    assertHuberTransactions(accounts);
  }


  public static void assertHuberTransactions(List<AccountImpl> accounts)
  {
    List<? extends Transaction> transactions = accounts.get(0)
        .getTransactions();
    TestCase.assertEquals(2, transactions.size());
    JaqlibAssert.assertEqualLists(AccountSetup.HUBER_ACCOUNT.getTransactions(),
        transactions);
  }

}
