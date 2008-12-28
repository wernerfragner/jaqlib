package org.jaqlib;

/**
 * @author Werner Fragner
 */
public class CustomConditonClassTest extends
    AbstractCustomConditonTest<AccountImpl>
{

  @Override
  protected Class<AccountImpl> getAccountClass()
  {
    return AccountImpl.class;
  }


}
