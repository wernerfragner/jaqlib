package org.jaqlib.query;

public interface ElementPredicate<T>
{

  boolean matches(T element);

}
