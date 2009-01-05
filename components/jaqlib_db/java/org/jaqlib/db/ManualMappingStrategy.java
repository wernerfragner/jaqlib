package org.jaqlib.db;

import java.sql.Types;
import java.util.List;

import org.jaqlib.util.Assert;
import org.jaqlib.util.CollectionUtil;

/**
 * Manually defines the mapping between database table columns and Jave bean
 * fields.
 * 
 * @author Werner Fragner
 */
public class ManualMappingStrategy implements MappingStrategy
{

  private final List<AbstractMapping<?>> mappings = CollectionUtil
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
    mapping.setFieldName(columnLabel);
    addMapping(mapping);

    return mapping;
  }


  private void addMapping(AbstractMapping<?> mapping)
  {
    Assert.notNull(mapping);
    mappings.add(mapping);
  }


  public List<AbstractMapping<?>> getMappings(Class<?> beanClass)
  {
    return mappings;
  }

}
