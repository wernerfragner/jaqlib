package org.jaqlib.core.bean;

import org.jaqlib.AccountImpl;
import org.jaqlib.NoDefaultConstructurClass;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class DefaultBeanFactoryTest
{

  private BeanFactory beanFactory;


  @BeforeEach
  protected void setUp()
  {
    beanFactory = new DefaultBeanFactory();
  }

  @Test
  public void testNewInstance()
  {
    assertEquals(AccountImpl.class, beanFactory.newInstance(AccountImpl.class)
        .getClass());
  }

  @Test
  public void testNewInstance_NoDefaultConstructor()
  {
    try
    {
      beanFactory.newInstance(NoDefaultConstructurClass.class);
      fail("Did not throw RuntimeException");
    }
    catch (RuntimeException e)
    {
      assertEquals(NoSuchMethodException.class, e.getCause().getClass());
    }
  }

}
