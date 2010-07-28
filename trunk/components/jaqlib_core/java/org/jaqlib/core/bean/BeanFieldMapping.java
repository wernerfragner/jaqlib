package org.jaqlib.core.bean;

/**
 * Represents a field that contains a bean (i.e. not a primitive type).
 * 
 * @author Werner Fragner
 * 
 * @param <T> the type of the field (= type of the bean).
 */
public class BeanFieldMapping<T> extends FieldMapping<T>
{

  private final BeanMapping<T> beanMapping;


  /**
   * Constructs a new field mapping by using the given name as source and target
   * name.
   * 
   * @param fieldName the Java bean field name.
   * @param fieldType the type of the Java bean field.
   */
  public BeanFieldMapping(String fieldName, Class<T> fieldType)
  {
    super(fieldName, fieldType);
    this.beanMapping = new BeanMapping<T>(fieldType);
  }


  public BeanMapping<T> getBeanMapping()
  {
    return beanMapping;
  }


}
