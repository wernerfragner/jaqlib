package org.jaqlib;

import org.jaqlib.core.AbstractQueryBuilder;
import org.jaqlib.core.WhereCondition;
import org.jaqlib.core.bean.BeanFactory;
import org.jaqlib.core.bean.BeanMapping;
import org.jaqlib.xml.XmlFromClause;
import org.jaqlib.xml.XmlQuery;
import org.jaqlib.xml.XmlWhereClause;


public class XmlQueryBuilder extends AbstractQueryBuilder
{

  /**
   * Contains the application wide default values for the XML query builder.
   */
  public static final XmlDefaults DEFAULTS = XmlDefaults.INSTANCE;


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
  public <T> XmlFromClause<T> select(Class<T> beanClass)
  {
    BeanMapping<T> beanMapping = getDefaultBeanMapping(beanClass);
    return select(beanMapping);
  }


  /**
   * This method basically provides the same functionality as
   * {@link #select(Class)}. But it gives more flexibility in defining the
   * mapping between XPath expression results to Java bean instance fields. This
   * mapping can defined with a {@link BeanMapping} instance. For building these
   * instances see {@link DatabaseQB}.
   * 
   * @param <T> the result bean type.
   * @param beanMapping a bean definition that holds information how to map the
   *          result of the SELECT statement to a Java bean.
   * @return the FROM clause to specify the database SELECT statement for the
   *         query.
   */
  public <T> XmlFromClause<T> select(BeanMapping<T> beanMapping)
  {
    return this.<T> createQuery(beanMapping).createFromClause();
  }


  private <T> XmlQuery<T> createQuery(BeanMapping<T> beanMapping)
  {
    return new XmlQuery<T>(getMethodCallRecorder(), beanMapping);
  }


}
