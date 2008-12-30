package org.jaqlib.db.java.typehandler;

/**
 * @author Werner Fragner
 */
public interface JavaTypeHandlerRegistry
{

  /**
   * @param fieldType a not null field type.
   * @return the type handler for the given field type. If no matching type
   *         handler was found then a {@link NullJavaTypeHandler} is returned.
   */
  JavaTypeHandler getTypeHandler(Class<?> fieldType);


  /**
   * Registers a custom java type handler with a given java type.
   * 
   * @param fieldType a not null java type.
   * @param typeHandler a not null custom java type handler.
   */
  void registerTypeHandler(Class<?> fieldType, JavaTypeHandler typeHandler);

}
