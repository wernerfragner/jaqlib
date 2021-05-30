package org.jaqlib.core;

import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.List;

import org.easymock.EasyMock;
import org.jaqlib.Account;
import org.jaqlib.AccountImpl;
import org.jaqlib.core.bean.BeanFactory;
import org.jaqlib.core.bean.BeanMapping;
import org.jaqlib.core.reflect.MethodInvocation;
import org.jaqlib.util.CollectionUtil;
import org.junit.jupiter.api.BeforeEach;

public class AbstractFetchStrategyTest
{

  protected final List<AccountImpl> accounts = CollectionUtil.newDefaultList();
  protected ElementPredicate<AccountImpl> predicate;


  public void setUp()
  {
    accounts.add(new AccountImpl());
    accounts.add(new AccountImpl());

    predicate = EasyMock.createMock(ElementPredicate.class);
  }


  protected void initStrategy(AbstractFetchStrategy<AccountImpl> strategy)
  {
    strategy.setDataSource(getSelectDataSource());
    strategy.setMapping(createMapping());
    strategy.setPredicate(predicate);
  }


  private SelectDataSource getSelectDataSource()
  {
    return new MockSelectDataSource();
  }


  private BeanMapping<AccountImpl> createMapping()
  {
    BeanMapping<AccountImpl> mapping = new BeanMapping<>(
        AccountImpl.class);
    mapping.setFactory(createBeanFactory());
    return mapping;
  }


  private BeanFactory createBeanFactory()
  {
    return new BeanFactory()
    {

      private int index = -1;


      @SuppressWarnings("unchecked")
      public <T> T newInstance(Class<T> beanClass)
      {
        index++;
        return (T) accounts.get(index);
      }

    };
  }


  protected MethodInvocation getMethodInvocation() throws NoSuchMethodException
  {
    Method method = AccountImpl.class.getMethod("getId");
    return new MethodInvocation(method, new Object[0]);
  }

}
