package org.jaqlib.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CollectionUtil
{

  public static <K, V> Map<K, V> newDefaultMap()
  {
    return new HashMap<K, V>();
  }


  public static <E> List<E> newDefaultList()
  {
    return new ArrayList<E>();
  }


  public static <E> Set<E> newDefaultSet()
  {
    return new HashSet<E>();
  }

}
