package org.jaqlib;

import java.util.ArrayList;
import java.util.List;


/**
 * @author Werner Fragner
 * 
 * @param <AccountType>
 */
@SuppressWarnings("unchecked")
public abstract class AbstractJaqLibTest<AccountType extends Account>
{

  protected abstract Class<AccountType> getAccountClass();


  public List<AccountType> createTestAccounts()
  {
    AccountImpl goodAccount = createAccount(10.0, "maier");
    goodAccount.setCreditRating(CreditRating.GOOD);

    List accounts = new ArrayList();
    accounts.add(createAccount(1.0, "huber"));
    accounts.add(goodAccount);
    accounts.add(createAccount(null, null));
    accounts.add(null);
    accounts.add(createAccount(5.0));
    accounts.add(null);
    return accounts;
  }

  public AccountImpl addElement(List elements, Double balance)
  {
    AccountImpl account = createAccount(balance);
    elements.add(account);
    return account;
  }


  protected AccountImpl createAccount(Double balance)
  {
    AccountImpl account = new AccountImpl();
    account.setBalance(balance);
    return account;
  }


  protected AccountImpl createAccount(Double balance, String lastName)
  {
    AccountImpl account = createAccount(balance);
    account.setLastName(lastName);
    return account;
  }

}
