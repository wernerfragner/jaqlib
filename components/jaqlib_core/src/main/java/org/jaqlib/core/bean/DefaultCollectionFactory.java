package org.jaqlib.core.bean;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import org.jaqlib.util.ReflectionUtil;

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
