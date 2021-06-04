package org.jaqlib;

public class AccountSetup
{

  public static final AccountImpl HUBER_ACCOUNT;
  public static final AccountImpl MAIER_ACCOUNT;

  public static final AccountImpl[] ACCOUNTS;


  static
  {
    HUBER_ACCOUNT = new AccountImpl();
    HUBER_ACCOUNT.setLastName("huber");
    HUBER_ACCOUNT.setFirstName("sepp");
    HUBER_ACCOUNT.setBalance(5000.0);
    HUBER_ACCOUNT.setCreditRating(CreditRating.GOOD);
    HUBER_ACCOUNT.setDepartment("linz");
    HUBER_ACCOUNT.addTransaction("1", 100.53);
    HUBER_ACCOUNT.addTransaction("2", -100);

    MAIER_ACCOUNT = new AccountImpl();
    MAIER_ACCOUNT.setLastName("maier");
    MAIER_ACCOUNT.setFirstName("franz");
    MAIER_ACCOUNT.setBalance(2000.0);
    MAIER_ACCOUNT.setCreditRating(CreditRating.POOR);
    MAIER_ACCOUNT.setDepartment("wien");
    MAIER_ACCOUNT.addTransaction("1", -500);
    MAIER_ACCOUNT.addTransaction("2", -1000);

    ACCOUNTS = new AccountImpl[] { HUBER_ACCOUNT, MAIER_ACCOUNT };
  }

}
