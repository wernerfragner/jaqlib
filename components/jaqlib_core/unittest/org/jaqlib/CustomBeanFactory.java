package org.jaqlib;

import org.jaqlib.core.bean.DefaultBeanFactory;

public class CustomBeanFactory extends DefaultBeanFactory
{

  private final EMailComponent emailComponent;


  public CustomBeanFactory(EMailComponent emailComponent)
  {
    this.emailComponent = emailComponent;
  }


  @Override
  public <T> T newInstance(Class<T> beanClass)
  {
    T instance = super.newInstance(beanClass);
    configureInstance(instance);
    return instance;
  }


  private void configureInstance(Object instance)
  {
    if (instance instanceof AccountImpl)
    {
      AccountImpl account = (AccountImpl) instance;
      account.setEMailComponent(emailComponent);
    }
  }

}
