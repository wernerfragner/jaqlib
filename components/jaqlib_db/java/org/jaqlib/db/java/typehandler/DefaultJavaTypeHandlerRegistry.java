package org.jaqlib.db.java.typehandler;

import static org.jaqlib.util.CollectionUtil.newDefaultMap;

import java.util.Map;

import org.jaqlib.util.Assert;

/**
 * @author Werner Fragner
 */
public class DefaultJavaTypeHandlerRegistry implements
    JavaTypeHandlerRegistry
{

  private final Map<Class<?>, JavaTypeHandler> handlers = newDefaultMap();
  private final JavaTypeHandler defaultHandler = new NullJavaTypeHandler();


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
  public void registerTypeHandler(Class<?> fieldType,
      JavaTypeHandler typeHandler)
  {
    Assert.notNull(fieldType);
    Assert.notNull(typeHandler);

    handlers.put(fieldType, typeHandler);
  }

}
