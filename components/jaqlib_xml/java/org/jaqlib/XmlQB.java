package org.jaqlib;

import org.jaqlib.core.WhereCondition;
import org.jaqlib.core.bean.BeanFactory;
import org.jaqlib.core.bean.BeanMapping;
import org.jaqlib.core.bean.MappingStrategy;
import org.jaqlib.util.FileResource;
import org.jaqlib.xml.XmlFromClause;
import org.jaqlib.xml.XmlSelectDataSource;
import org.jaqlib.xml.XmlWhereClause;

/**
 * Helper class with static methods for XML query support (see
 * {@link XmlQueryBuilder} for further information).
 * 
 * <p>
 * This class provides static helper methods to easily execute queries against
 * XML files. <br>
 * Examples are given here: {@link XmlQueryBuilder}.
 * </p>
 * This class is thread-safe.
 * 
 * @see XmlQueryBuilder
 * @see XmlDefaults
 * @author Werner Fragner
 */
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


  /**
   * <p>
   * Uses a given XPath expression to fill a user-defined Java bean. First the
   * XML file has to be specified in the returned {@link XmlFromClause}. Then
   * the XPath expression must be specified in the {@link XmlWhereClause}.
   * Afterwards you can specify arbitrary WHERE conditions. These WHERE
   * conditions support AND and OR connectors, the evaluation of custom
   * {@link WhereCondition}s and custom conditions using a method call recording
   * mechanism (see examples and {@link #getRecorder(Class)} for further
   * details).
   * </p>
   * <p>
   * <b>NOTE: the WHERE condition is not executed by the XPath engine but at
   * Java side. Avoid executing XPath expressions with lots of data and then
   * constraining it with the WHERE functionality of JaqLib!</b>.
   * </p>
   * 
   * @param <T> the result bean type.
   * @param beanClass the desired result bean. This bean must provide a default
   *          constructor. If the bean does not provide one a custom
   *          {@link BeanFactory} must be registered at the {@link BeanMapping}.
   *          This {@link BeanMapping} can be obtained from {@link Database} and
   *          can be used with {@link #select(BeanMapping)}.
   * @return the FROM clause to specify the input XML file for the query.
   */
  public static <T> XmlFromClause<T> select(Class<T> beanClass)
  {
    return getQueryBuilder().select(beanClass);
  }


  /**
   * Creates a new {@link XmlSelectDataSource} using the given XML path. The
   * returned data source uses XML attributes to fill java bean fields.
   * 
   * @param xmlPath the path to the XML file.
   * @return a new {@link XmlSelectDataSource}. This instance can be changed by
   *         using the various setter methods and can be re-used for multiple
   *         queries.
   */
  public static XmlSelectDataSource getSelectDataSource(String xmlPath)
  {
    return new XmlSelectDataSource(new FileResource(xmlPath));
  }


  /**
   * See {@link BeanMapping#build(Class)}.
   */
  public static <T> BeanMapping<T> getDefaultBeanMapping(
      Class<? extends T> beanClass)
  {
    return getQueryBuilder().getDefaultBeanMapping(beanClass);
  }


  /**
   * See {@link BeanMapping#build(MappingStrategy, Class)}.
   */
  public static <T> BeanMapping<T> getBeanMapping(
      MappingStrategy mappingStrategy, Class<? extends T> beanClass)
  {
    return getQueryBuilder().getBeanMapping(mappingStrategy, beanClass);
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

}
