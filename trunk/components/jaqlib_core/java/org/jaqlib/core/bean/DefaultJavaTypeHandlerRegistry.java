package org.jaqlib.core.bean;

import static org.jaqlib.util.CollectionUtil.newDefaultMap;

import java.util.Map;

import org.jaqlib.util.Assert;

/**
 * @author Werner Fragner
 */
public class DefaultJavaTypeHandlerRegistry implements JavaTypeHandlerRegistry
{

  private final Map<Class<?>, JavaTypeHandler> handlers = newDefaultMap();
  private final JavaTypeHandler defaultHandler = JavaTypeHandler.NULL;


  /**
   * {@inheritDoc}
   */
  public JavaTypeHandler getTypeHandler(Class<?> fieldType)
  {
    Assert.notNull(fieldType);

    final JavaTypeHandler handler = handlers.get(fieldType);
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
  public void registerTypeHandler(JavaTypeHandler typeHandler)
  {
    Assert.notNull(typeHandler);

    for (Class<?> fieldType : typeHandler.getSupportedTypes())
    {
      handlers.put(fieldType, typeHandler);
    }
  }

}
