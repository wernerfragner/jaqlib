package org.jaqlib.db;

import java.util.List;

import junit.framework.TestCase;

import org.jaqlib.core.Defaults;
import org.jaqlib.core.bean.BeanFactory;
import org.jaqlib.core.bean.FieldMapping;
import org.jaqlib.core.bean.JavaTypeHandler;
import org.jaqlib.core.bean.JavaTypeHandlerRegistry;
import org.jaqlib.core.bean.MappingStrategy;
import org.jaqlib.db.sql.typehandler.SqlTypeHandler;
import org.jaqlib.db.sql.typehandler.SqlTypeHandlerRegistry;

public class DefaultsTest extends TestCase
{


  @Override
  public void tearDown()
  {
    DbDefaults.reset();
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
    assertNotNull(Defaults.getMappingStrategy());
  }


  public void testSetMappingStrategy_Null()
  {
    try
    {
      Defaults.setMappingStrategy(null);
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
    MappingStrategy strategy = new MappingStrategy()
    {

      public List<FieldMapping<?>> getMappings(Class<?> beanClass)
      {
        return null;
      }

    };
    Defaults.setMappingStrategy(strategy);
    assertSame(strategy, Defaults.getMappingStrategy());
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


  public void testGetSqlTypeHandlerRegistry()
  {
    assertNotNull(DbDefaults.getSqlTypeHandlerRegistry());
  }


  public void testSetSqlTypeHandlerRegistry_Null()
  {
    try
    {
      DbDefaults.setSqlTypeHandlerRegistry(null);
      fail("Did not throw IllegalArgumentException");
    }
    catch (IllegalArgumentException e)
    {
    }
  }


  public void testSetSqlTypeHandlerRegistry()
  {
    SqlTypeHandlerRegistry registry = new SqlTypeHandlerRegistry()
    {

      public SqlTypeHandler getTypeHandler(int sqlDataType)
      {
        return null;
      }


      public void registerTypeHandler(int sqlDataType,
          SqlTypeHandler typeHandler)
      {
      }

    };
    DbDefaults.setSqlTypeHandlerRegistry(registry);
    assertSame(registry, DbDefaults.getSqlTypeHandlerRegistry());
  }


  public void testSetStrictColumnCheck()
  {
    boolean prevValue = Defaults.getStrictColumnCheck();
    try
    {
      assertFalse(Defaults.getStrictColumnCheck());
      Defaults.setStrictColumnCheck(true);
      assertTrue(Defaults.getStrictColumnCheck());
    }
    finally
    {
      // clean up
      Defaults.setStrictColumnCheck(prevValue);
    }
  }


  public void testGetStrictColumnCheck()
  {
    assertFalse(Defaults.getStrictColumnCheck());
  }

}
