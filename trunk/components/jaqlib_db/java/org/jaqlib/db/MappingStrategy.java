package org.jaqlib.db;

public interface MappingStrategy
{

  <T> void execute(ComplexDbSelectResult<T> result);

}
