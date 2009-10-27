package org.jaqlib;

import java.util.List;


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


  /**
   * It's tried to invoke a non-public method on the recorder. An
   * {@link IllegalAccessException} must be thrown.
   */
  public void testSelect_ReflectiveCondition_IllegalAccessException()
  {
    AccountImpl dummy = IterableQB.getRecorder(getAccountClass());
    final Department department = new Department("athome");

    List<AccountImpl> accounts = createTestAccounts();
    try
    {
      IterableQB.selectFrom(accounts).whereCall(dummy.getDepartmentProtected())
          .isEqual(department).firstResult();
      fail("Did not throw RuntimeException");
    }
    catch (RuntimeException e)
    {
      assertEquals(IllegalAccessException.class, e.getCause().getClass());
    }
  }

}
