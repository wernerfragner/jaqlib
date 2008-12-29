package org.jaqlib;


/**
 * @author Werner Fragner
 */
public class ReflectiveConditionClassTest extends
    AbstractReflectiveConditionTest<AccountImpl>
{

  @Override
  protected Class<AccountImpl> getAccountClass()
  {
    return AccountImpl.class;
  }


  @Override
  protected AccountImpl createAccountType(Double balance)
  {
    return createAccount(balance);
  }

}
