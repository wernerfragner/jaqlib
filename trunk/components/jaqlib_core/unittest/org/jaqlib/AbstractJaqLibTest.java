package org.jaqlib;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

/**
 * @author Werner Fragner
 * 
 * @param <AccountType>
 */
@SuppressWarnings("unchecked")
public abstract class AbstractJaqLibTest<AccountType extends Account> extends
    TestCase
{

  protected abstract Class<AccountType> getAccountClass();


  public List<AccountType> createTestAccounts()
  {
    List accounts = new ArrayList();
    accounts.add(createAccount(1, "huber"));
    accounts.add(createAccount(10, "maier"));
    accounts.add(createAccount(null, null));
    accounts.add(null);
    accounts.add(createAccount(5));
    accounts.add(null);
    return accounts;
  }


  public AccountImpl addElement(List elements, int balance)
  {
    AccountImpl account = createAccount(balance);
    elements.add(account);
    return account;
  }


  protected AccountImpl createAccount(Integer balance)
  {
    AccountImpl account = new AccountImpl();
    account.setBalance(balance);
    return account;
  }


  protected AccountImpl createAccount(Integer balance, String lastName)
  {
    AccountImpl account = createAccount(balance);
    account.setLastName(lastName);
    return account;
  }

}
