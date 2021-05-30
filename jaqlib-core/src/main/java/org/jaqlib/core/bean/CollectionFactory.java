package org.jaqlib.core.bean;

import java.util.Collection;


/**
 * Abstracts the creation of {@link Collection} instances. This interface can be
 * used if you want to take control of the {@link Collection} instantiation by
 * Jaqlib. This can be necessary if you want to instantiate your own collection
 * classes instead of the default ones (e.g., java.util.List -->
 * my.MyArrayList).
 * 
 * @author Werner Fragner
 */
public interface CollectionFactory
{

  /**
   * @param collectionClass a not null {@link Collection} class.
   * @return a new instance of the given class; if the given class is an
   *         interface or if it is abstract then a default implementation for
   *         the given collection must be returned.
   */
  Collection<?> newInstance(Class<? extends Collection<?>> collectionClass);

}
