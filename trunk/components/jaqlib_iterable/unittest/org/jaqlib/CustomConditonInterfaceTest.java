package org.jaqlib;

/**
 * @author Werner Fragner
 */
public class CustomConditonInterfaceTest extends
    AbstractCustomConditonTest<Account>
{

  @Override
  protected Class<Account> getAccountClass()
  {
    return Account.class;
  }


}
