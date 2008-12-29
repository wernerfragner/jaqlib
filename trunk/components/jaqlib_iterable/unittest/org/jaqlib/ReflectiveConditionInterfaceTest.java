package org.jaqlib;


/**
 * @author Werner Fragner
 */
public class ReflectiveConditionInterfaceTest extends
    AbstractReflectiveConditionTest<Account>
{

  @Override
  protected Class<Account> getAccountClass()
  {
    return Account.class;
  }


  @Override
  protected Account createAccountType(Integer balance)
  {
    return createAccount(balance);
  }

}
