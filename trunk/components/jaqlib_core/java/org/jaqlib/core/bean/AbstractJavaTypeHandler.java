package org.jaqlib.core.bean;

import java.util.List;

import org.jaqlib.util.CollectionUtil;
import org.jaqlib.util.ReflectionUtil;

/**
 * Abstract base class for Java type handlers that convert data types from the
 * source to Java bean field data types. It provides some common functionality
 * for Java type handlers.
 * 
 * @see JavaTypeHandler
 * @author Werner Fragner
 */
public abstract class AbstractJavaTypeHandler implements JavaTypeHandler
{

  /**
   * {@inheritDoc}
   */
  public final Class<?>[] getSupportedTypes()
  {
    List<Class<?>> types = CollectionUtil.newDefaultList();
    addSupportedTypes(types);
    return types.toArray(new Class[types.size()]);
  }


  /**
   * Extending class must implement this method. Extending classes must add all
   * supported types to the given list.
   * 
   * @param types a list to which all supported types must be added. The given
   *          list is never null.
   */
  protected abstract void addSupportedTypes(List<Class<?>> types);


  /**
   * Helper method to handle an illegal input value.
   * 
   * @param value the illegal input value.
   * @param expectedClass the class that was expected.
   * @return the standard exception for this error case.
   */
  protected RuntimeException handleIllegalInputValue(Object value,
      Class<?> expectedClass)
  {
    final String valueClassName = ReflectionUtil.getPlainClassName(value);
    final String requiredClassName = ReflectionUtil
        .getPlainClassName(expectedClass);

    return new IllegalArgumentException("Cannot convert a '" + valueClassName
        + "' to a '" + requiredClassName + "'.");
  }

}
