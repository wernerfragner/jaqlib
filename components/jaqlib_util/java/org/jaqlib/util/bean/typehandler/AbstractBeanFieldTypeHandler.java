package org.jaqlib.util.bean.typehandler;

import org.jaqlib.util.reflect.ReflectionUtil;

/**
 * @author Werner Fragner
 */
public abstract class AbstractBeanFieldTypeHandler implements
    BeanFieldTypeHandler
{

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
