package org.jaqlib;

import java.util.List;

import junit.framework.TestCase;

import org.jaqlib.core.Defaults;
import org.jaqlib.core.bean.BeanFactory;
import org.jaqlib.core.bean.FieldMapping;
import org.jaqlib.core.bean.JavaTypeHandler;
import org.jaqlib.core.bean.JavaTypeHandlerRegistry;
import org.jaqlib.core.bean.BeanMappingStrategy;

public class DefaultsTest extends TestCase
{


  @Override
  public void tearDown()
  {
    Defaults.reset();
  }


  public void testGetBeanFactory()
  {
    assertNotNull(Defaults.getBeanFactory());
  }


  public void testSetBeanFactory_Null()
  {
    try
    {
      Defaults.setBeanFactory(null);
      fail("Did not throw IllegalArgumentException");
    }
    catch (IllegalArgumentException e)
    {
    }
  }


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


  public void testGetMappingStrategy()
  {
    assertNotNull(Defaults.getBeanMappingStrategy());
  }


  public void testSetMappingStrategy_Null()
  {
    try
    {
      Defaults.setBeanMappingStrategy(null);
      fail("Did not throw IllegalArgumentException");
    }
    catch (IllegalArgumentException e)
    {
    }
    finally
    {

    }
  }


  public void testSetMappingStrategy()
  {
    BeanMappingStrategy strategy = new BeanMappingStrategy()
    {

      public List<FieldMapping<?>> getMappings(Class<?> beanClass)
      {
        return null;
      }

    };
    Defaults.setBeanMappingStrategy(strategy);
    assertSame(strategy, Defaults.getBeanMappingStrategy());
  }


  public void testGetJavaTypeHandlerRegistry()
  {
    assertNotNull(Defaults.getJavaTypeHandlerRegistry());
  }


  public void testSetJavaTypeHandlerRegistry_Null()
  {
    try
    {
      Defaults.setJavaTypeHandlerRegistry(null);
      fail("Did not throw IllegalArgumentException");
    }
    catch (IllegalArgumentException e)
    {
    }
  }


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


  public void testGetStrictColumnCheck()
  {
    assertFalse(Defaults.getStrictFieldCheck());
  }

}
