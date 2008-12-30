package org.jaqlib.util.bean.typehandler;

/**
 * @author Werner Fragner
 */
public interface BeanFieldTypeHandlerRegistry
{

  /**
   * @param fieldType a not null field type.
   * @return the type handler for the given field type. If no matching type
   *         handler was found then a {@link NullBeanFieldTypeHandler} is
   *         returned.
   */
  BeanFieldTypeHandler getTypeHandler(Class<?> fieldType);


  /**
   * Registers a custom bean field type handler with a given bean field type.
   * 
   * @param fieldType a not null bean field type.
   * @param typeHandler a not null custom bean field type handler.
   */
  void registerTypeHandler(Class<?> fieldType, BeanFieldTypeHandler typeHandler);

}
