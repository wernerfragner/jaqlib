package org.jaqlib.core.bean;

import org.jaqlib.util.ReflectionUtil;

import java.util.*;

/**
 * Default implementation of the {@link CollectionFactory} interface. It tries
 * to instantiate the given collection class. If this is not possible the
 * default collection class is instantiated. If this does not succeed either
 * then an {@link IllegalArgumentException} is thrown.
 * 
 * @author Werner Fragner
 */
public class DefaultCollectionFactory implements CollectionFactory
{

  /**
   * {@inheritDoc}
   */
  public Collection<?> newInstance(
      Class<? extends Collection<?>> collectionClass)
  {
    if (ReflectionUtil.isAbstract(collectionClass))
    {
      return createDefaultCollection(collectionClass);
    }
    else
    {
      return ReflectionUtil.newInstance(collectionClass);
    }
  }


  protected Collection<?> createDefaultCollection(
      Class<? extends Collection<?>> collectionClass)
  {
    if (collectionClass.equals(List.class))
    {
      return new ArrayList<>();
    }
    else if (collectionClass.equals(Set.class))
    {
      return new HashSet<>();
    }
    else if (collectionClass.equals(Queue.class))
    {
      return new LinkedList<>();
    }

    throw new IllegalArgumentException(
        "Given collection class is not supported: " + collectionClass);
  }
}
