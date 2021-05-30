package org.jaqlib;


import org.jaqlib.core.Defaults;
import org.jaqlib.core.bean.BeanFactory;
import org.jaqlib.core.bean.BeanMappingStrategy;
import org.jaqlib.core.bean.JavaTypeHandler;
import org.jaqlib.core.bean.JavaTypeHandlerRegistry;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DefaultsTest
{


  @AfterEach
  public void tearDown()
  {
    Defaults.reset();
  }


  @Test
  public void testGetBeanFactory()
  {
    assertNotNull(Defaults.getBeanFactory());
  }

  @Test
  public void testSetBeanFactory_Null()
  {
    try
    {
      Defaults.setBeanFactory(null);
      fail("Did not throw IllegalArgumentException");
    }
    catch (IllegalArgumentException e)
    {
      // expected
    }
  }

  @Test
  public void testSetBeanFactory()
  {
    BeanFactory factory = new BeanFactory()
    {

      public <T> T newInstance(Class<T> beanClass)
      {
        return null;
      }

    };

    BeanFactory prev = Defaults.getBeanFactory();

    Defaults.setBeanFactory(factory);
    assertSame(factory, Defaults.getBeanFactory());

    Defaults.setBeanFactory(prev);
  }

  @Test
  public void testGetMappingStrategy()
  {
    assertNotNull(Defaults.getBeanMappingStrategy());
  }

  @Test
  public void testSetMappingStrategy_Null()
  {
    try
    {
      Defaults.setBeanMappingStrategy(null);
      fail("Did not throw IllegalArgumentException");
    }
    catch (IllegalArgumentException e)
    {
      // expected
    }
  }

  @Test
  public void testSetMappingStrategy()
  {
    BeanMappingStrategy strategy = beanClass -> null;
    Defaults.setBeanMappingStrategy(strategy);
    assertSame(strategy, Defaults.getBeanMappingStrategy());
  }

  @Test
  public void testGetJavaTypeHandlerRegistry()
  {
    assertNotNull(Defaults.getJavaTypeHandlerRegistry());
  }

  @Test
  public void testSetJavaTypeHandlerRegistry_Null()
  {
    try
    {
      Defaults.setJavaTypeHandlerRegistry(null);
      fail("Did not throw IllegalArgumentException");
    }
    catch (IllegalArgumentException e)
    {
      // expected
    }
  }

  @Test
  public void testSetJavaTypeHandlerRegistry()
  {
    JavaTypeHandlerRegistry registry = new JavaTypeHandlerRegistry()
    {

      public JavaTypeHandler getTypeHandler(Class<?> fieldType)
      {
        return null;
      }


      public void registerTypeHandler(JavaTypeHandler typeHandler)
      {
      }
    };
    JavaTypeHandlerRegistry previous = Defaults.getJavaTypeHandlerRegistry();
    Defaults.setJavaTypeHandlerRegistry(registry);
    assertSame(registry, Defaults.getJavaTypeHandlerRegistry());

    Defaults.setJavaTypeHandlerRegistry(previous);
  }

  @Test
  public void testSetStrictColumnCheck()
  {
    boolean prevValue = Defaults.getStrictFieldCheck();
    try
    {
      assertFalse(Defaults.getStrictFieldCheck());
      Defaults.setStrictFieldCheck(true);
      assertTrue(Defaults.getStrictFieldCheck());
    }
    finally
    {
      // clean up
      Defaults.setStrictFieldCheck(prevValue);
    }
  }

  @Test
  public void testGetStrictColumnCheck()
  {
    assertFalse(Defaults.getStrictFieldCheck());
  }

}
