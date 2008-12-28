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


  public List<AccountType> createIsMatchElements()
  {
    AccountImpl testClass1 = new AccountImpl(false);
    AccountImpl testClass2 = new AccountImpl(true);
    AccountImpl testClass3 = null;
    AccountImpl testClass4 = new AccountImpl(false);

    List elements = new ArrayList();
    elements.add(testClass1);
    elements.add(testClass2);
    elements.add(testClass3);
    elements.add(testClass4);
    return elements;
  }


  public List<AccountType> createGetObjectElements()
  {
    AccountImpl testClass1 = new AccountImpl(new Object());
    AccountImpl testClass2 = new AccountImpl(new Object());
    AccountImpl testClass3 = null;
    AccountImpl testClass4 = new AccountImpl(null);

    List elements = new ArrayList();
    elements.add(testClass1);
    elements.add(testClass2);
    elements.add(testClass3);
    elements.add(testClass4);
    return elements;
  }


  public List<AccountType> createGetCompareElements()
  {
    AccountImpl testClass1 = new AccountImpl(1);
    AccountImpl testClass2 = new AccountImpl(10);
    AccountImpl testClass3 = new AccountImpl(null);
    AccountImpl testClass4 = null;
    AccountImpl testClass5 = new AccountImpl(5);

    List elements = new ArrayList();
    elements.add(testClass1);
    elements.add(testClass2);
    elements.add(testClass3);
    elements.add(testClass4);
    elements.add(testClass5);
    return elements;
  }


  public List<AccountType> createListWithNulls()
  {
    List elements = new ArrayList();
    elements.add(new AccountImpl());
    elements.add(null);
    elements.add(null);
    elements.add(new AccountImpl());
    return elements;
  }


  public void addElement(List elements, int balance)
  {
    elements.add(new AccountImpl(balance));
  }

}
