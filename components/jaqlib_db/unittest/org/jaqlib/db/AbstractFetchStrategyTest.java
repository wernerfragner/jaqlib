package org.jaqlib.db;

import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.List;

import junit.framework.TestCase;

import org.easymock.EasyMock;
import org.jaqlib.AccountImpl;
import org.jaqlib.DatabaseSetup;
import org.jaqlib.core.AbstractFetchStrategy;
import org.jaqlib.core.ElementPredicate;
import org.jaqlib.core.bean.BeanFactory;
import org.jaqlib.core.bean.BeanMapping;
import org.jaqlib.core.reflect.MethodInvocation;
import org.jaqlib.util.CollectionUtil;

public class AbstractFetchStrategyTest extends TestCase
{

  protected final List<AccountImpl> accounts = CollectionUtil.newDefaultList();
  protected ElementPredicate<AccountImpl> predicate;


  @Override
  protected void setUp() throws Exception
  {
    super.setUp();

    accounts.add(new AccountImpl());
    accounts.add(new AccountImpl());

    predicate = EasyMock.createMock(ElementPredicate.class);
  }


  protected void initStrategy(AbstractFetchStrategy<AccountImpl> strategy)
      throws SQLException
  {
    strategy.setDataSource(DatabaseSetup.getDbSelectDataSource());
    strategy.setMapping(createMapping());
    strategy.setPredicate(predicate);
  }


  private BeanMapping<AccountImpl> createMapping()
  {
    BeanMapping<AccountImpl> mapping = new BeanMapping<AccountImpl>(
        AccountImpl.class);
    mapping.setBeanFactory(createBeanFactory());
    return mapping;
  }


  private BeanFactory createBeanFactory()
  {
    return new BeanFactory()
    {

      private int index = -1;


      public <T> T newInstance(Class<T> beanClass)
      {
        index++;
        return (T) accounts.get(index);
      }

    };
  }


  protected MethodInvocation getMethodInvocation() throws NoSuchMethodException
  {
    Method method = AccountImpl.class.getMethod("getId", new Class[0]);
    return new MethodInvocation(method, new Object[0]);
  }

}
