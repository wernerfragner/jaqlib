package org.jaqlib.core.bean;

import org.jaqlib.AccountImpl;
import org.jaqlib.core.bean.BeanFactory;
import org.jaqlib.core.bean.DefaultBeanFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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
      beanFactory.newInstance(NoDefaultConstructorClass.class);
      fail("Did not throw RuntimeException");
    }
    catch (RuntimeException e)
    {
      assertEquals(NoSuchMethodException.class, e.getCause().getClass());
    }
  }


  private static class NoDefaultConstructorClass
  {

    @SuppressWarnings("unused")
    public NoDefaultConstructorClass(String arg)
    {
    }

  }

}
