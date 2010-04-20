package org.jaqlib;

import org.jaqlib.util.FileResource;
import org.jaqlib.xml.XmlFromClause;
import org.jaqlib.xml.XmlSelectDataSource;


public class XmlQB
{

  /**
   * This class is not intended to be instantiated.
   */
  private XmlQB()
  {
    throw new UnsupportedOperationException(
        "This class is not intended to be instantiated");
  }

  /**
   * Holds singleton instances per thread.
   */
  private static final ThreadLocal<XmlQueryBuilder> QUERYBUILDER = new ThreadLocal<XmlQueryBuilder>();


  /**
   * Sets a user-defined classloader that is used when creating proxy classes
   * using the {@link #getRecorder(Class)} method.
   * 
   * @param classLoader a not null class loader.
   */
  public static void setClassLoader(ClassLoader classLoader)
  {
    getQueryBuilder().setClassLoader(classLoader);
  }


  /**
   * @param <T> the type of the result element(s).
   * @param resultElementClass a not null class of the result element.
   * @return a proxy object that records all method calls. These calls are used
   *         when evaluating the WHERE clause of a query (see examples).
   */
  public static <T> T getRecorder(Class<T> resultElementClass)
  {
    return getQueryBuilder().getRecorder(resultElementClass);
  }


  public static <T> XmlFromClause<T> select(Class<T> beanClass)
  {
    return getQueryBuilder().select(beanClass);
  }


  /**
   * See return tag.
   * 
   * @return the query builder instance for the current thread.
   */
  static XmlQueryBuilder getQueryBuilder()
  {
    XmlQueryBuilder queryBuilder = QUERYBUILDER.get();
    if (queryBuilder == null)
    {
      queryBuilder = new XmlQueryBuilder();
      QUERYBUILDER.set(queryBuilder);
    }
    return queryBuilder;
  }


  public static XmlSelectDataSource getSelectDataSource(String filePath)
  {
    return new XmlSelectDataSource(new FileResource(filePath));
  }

}
