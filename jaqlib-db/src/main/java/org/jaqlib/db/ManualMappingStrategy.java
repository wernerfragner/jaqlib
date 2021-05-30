package org.jaqlib.db;

import org.jaqlib.core.bean.BeanMappingStrategy;
import org.jaqlib.core.bean.FieldMapping;
import org.jaqlib.util.Assert;
import org.jaqlib.util.CollectionUtil;

import java.sql.Types;
import java.util.List;

/**
 * <p>
 * Manually defines the mapping between database table columns and Java bean
 * fields.
 * </p>
 * <p>
 * <b>NOTE: This strategy is only applicable for the database functionality of
 * Jaqlib!</b>
 * </p>
 * 
 * @author Werner Fragner
 */
public class ManualMappingStrategy implements BeanMappingStrategy
{

  private final List<FieldMapping<?>> mappings = CollectionUtil
      .newDefaultList();


  public ColumnMapping<?> addColumnMapping(String columnLabel)
  {
    return addColumnMapping(columnLabel, columnLabel);
  }


  public ColumnMapping<?> addColumnMapping(String columnName, String columnLabel)
  {
    return addColumnMapping(columnName, columnLabel, Types.OTHER);
  }


  public ColumnMapping<?> addColumnMapping(String columnName,
      String columnLabel, int sqlDataType)
  {
    Assert.notNull(columnName);
    Assert.notNull(columnLabel);

    ColumnMapping<?> mapping = new ColumnMapping<Object>();
    mapping.setColumnName(columnName);
    mapping.setColumnLabel(columnLabel);
    mapping.setColumnDataType(sqlDataType);
    mapping.setTargetName(columnLabel);
    addColumnMapping(mapping);

    return mapping;
  }


  public void addColumnMapping(ColumnMapping<?> mapping)
  {
    addMapping(mapping);
  }


  public void addMapping(ColumnMapping<?> mapping)
  {
    Assert.notNull(mapping);
    mappings.add(mapping);
  }


  public void addMappings(List<FieldMapping<?>> fieldMappings)
  {
    this.mappings.addAll(fieldMappings);
  }


  /**
   * {@inheritDoc}
   */
  public List<FieldMapping<?>> getMappings(Class<?> beanClass)
  {
    return mappings;
  }

}
