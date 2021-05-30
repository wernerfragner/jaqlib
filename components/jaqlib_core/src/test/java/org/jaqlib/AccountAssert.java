package org.jaqlib;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class AccountAssert
{

  public static void assertMaierAccount(Account account)
  {
    assertNotNull(account);
    assertEqualAccount(AccountSetup.MAIER_ACCOUNT, account);
  }


  public static void assertHuberAccount(Account account)
  {
    assertNotNull(account);
    assertEqualAccount(AccountSetup.HUBER_ACCOUNT, account);
  }


  public static void assertEqualAccount(Account expected, Account given)
  {
    assertEquals(expected.getLastName(), given.getLastName());
    assertEquals(expected.getFirstName(), given.getFirstName());
    assertEquals(expected.getBalance(), given.getBalance());
    assertEquals(expected.getCreditRating(), given.getCreditRating());
  }


  public static void assertMaierAccount(List<AccountImpl> accounts)
  {
    assertNotNull(accounts);
    assertEquals(1, accounts.size());
    AccountAssert.assertMaierAccount(accounts.get(0));
  }


  public static void assertHuberAccount(List<AccountImpl> accounts)
  {
    assertNotNull(accounts);
    assertEquals(1, accounts.size());
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
    assertEquals(2, transactions.size());
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
    assertEquals(2, transactions.size());
    JaqlibAssert.assertEqualLists(AccountSetup.HUBER_ACCOUNT.getTransactions(),
        transactions);
  }

}
