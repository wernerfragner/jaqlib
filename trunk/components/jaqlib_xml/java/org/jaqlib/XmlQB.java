package org.jaqlib;

import org.jaqlib.core.WhereCondition;
import org.jaqlib.core.bean.BeanFactory;
import org.jaqlib.core.bean.BeanMapping;
import org.jaqlib.xml.XmlFromClause;
import org.jaqlib.xml.XmlWhereClause;

/**
 * <p>
 * This class provides static helper methods to easily execute queries against
 * XML files. <br>
 * Examples are given here: {@link XmlQueryBuilder}.
 * </p>
 * <p>
 * This class is thread-safe.
 * </p>
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
   * @param recorderClass a not null class of element whose method calls should
   *          be recorded.
   * @return a proxy object that records all method calls. These calls are used
   *         when evaluating the WHERE clause of a query (see examples in
   *         {@link XmlQueryBuilder}).
   */
  public static <T> T getRecorder(Class<T> recorderClass)
  {
    return getQueryBuilder().getRecorder(recorderClass);
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
   *          A {@link BeanMapping} can be simply instantiated with the
   *          <tt>new</tt> operator. This {@link BeanMapping} can be used in the
   *          {@link #select(BeanMapping)} method.
   * @return the FROM clause to specify the input XML file for the query.
   */
  public static <T> XmlFromClause<T> select(Class<T> beanClass)
  {
    return getQueryBuilder().select(beanClass);
  }


  /**
   * This method basically provides the same functionality as
   * {@link #select(Class)}. But it gives more flexibility in defining the
   * mapping between XPath expression results and Java bean fields. This mapping
   * can defined with a {@link BeanMapping} instance. A {@link BeanMapping} can
   * be simply instantiated with the <tt>new</tt> operator.
   * 
   * @param <T> the result bean type.
   * @param mapping a bean definition that holds information how to map the
   *          result of the query to a Java bean.
   * @return the FROM clause to specify the input XML file for the query.
   */
  public static <T> XmlFromClause<T> select(BeanMapping<T> mapping)
  {
    return getQueryBuilder().select(mapping);
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
