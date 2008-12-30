package org.jaqlib.util.bean.typehandler;

import static org.jaqlib.util.CollectionUtil.newDefaultMap;

import java.util.Map;

import org.jaqlib.util.Assert;

/**
 * @author Werner Fragner
 */
public class DefaultBeanFieldTypeHandlerRegistry implements
    BeanFieldTypeHandlerRegistry
{

  private final Map<Class<?>, BeanFieldTypeHandler> handlers = newDefaultMap();
  private final BeanFieldTypeHandler defaultHandler = new NullBeanFieldTypeHandler();


  /**
   * {@inheritDoc}
   */
  public BeanFieldTypeHandler getTypeHandler(Class<?> fieldType)
  {
    Assert.notNull(fieldType);

    final BeanFieldTypeHandler handler = handlers.get(fieldType);
    if (handler != null)
    {
      return handler;
    }
    else
    {
      return defaultHandler;
    }
  }


  /**
   * {@inheritDoc}
   */
  public void registerTypeHandler(Class<?> fieldType,
      BeanFieldTypeHandler typeHandler)
  {
    Assert.notNull(fieldType);
    Assert.notNull(typeHandler);

    handlers.put(fieldType, typeHandler);
  }

}
