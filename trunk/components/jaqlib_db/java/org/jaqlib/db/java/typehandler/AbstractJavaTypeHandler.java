package org.jaqlib.db.java.typehandler;

import java.util.List;

import org.jaqlib.util.CollectionUtil;
import org.jaqlib.util.ReflectionUtil;

/**
 * @author Werner Fragner
 */
public abstract class AbstractJavaTypeHandler implements JavaTypeHandler
{


  public final Class<?>[] getSupportedTypes()
  {
    List<Class<?>> types = CollectionUtil.newDefaultList();
    addSupportedTypes(types);
    return types.toArray(new Class[types.size()]);
  }


  protected abstract void addSupportedTypes(List<Class<?>> types);


  protected RuntimeException handleIllegalInputValue(Object value,
      Class<?> requiredClass)
  {
    final String valueClassName = ReflectionUtil.getPlainClassName(value);
    final String requiredClassName = ReflectionUtil
        .getPlainClassName(requiredClass);

    return new IllegalArgumentException("Cannot convert a '" + valueClassName
        + "' to a '" + requiredClassName + "'.");
  }

}
