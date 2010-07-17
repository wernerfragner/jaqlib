package org.jaqlib;

import org.jaqlib.core.AbstractQueryBuilder;
import org.jaqlib.core.WhereCondition;
import org.jaqlib.core.bean.BeanFactory;
import org.jaqlib.core.bean.BeanMapping;
import org.jaqlib.core.bean.BeanMappingStrategy;
import org.jaqlib.util.ClassPathResource;
import org.jaqlib.util.FileResource;
import org.jaqlib.xml.XmlFromClause;
import org.jaqlib.xml.XmlQuery;
import org.jaqlib.xml.XmlSelectDataSource;
import org.jaqlib.xml.XmlWhereClause;
import org.jaqlib.xml.xpath.XPathEngine;

/**
 * <h2>Overview</h2>
 * <p>
 * The main entry point of JaQLib for XML query support. It provides following
 * methods for building queries:
 * <ul>
 * <li>{@link #select(Class)}</li>
 * <li>{@link #select(BeanMapping)}</li>
 * </ul>
 * </p>
 * <p>
 * The mapping between XML data and Java bean fields is done using XML elements
 * or XML attributes. For example, if you have a XML element 'account' with sub
 * elements 'lastName', 'balance' then you would have to have a class with the
 * fields 'lastName' and 'balance'. The same applies when you use XML attributes
 * instead of XML elements.
 * 
 * <pre>
 * <account>
 *   <lastName>Fragner</lastName>
 *   <balance>-5000.0</balance>
 * </account>
 * </pre>
 * 
 * or
 * 
 * <pre>
 * <![CDATA[
 * <account lastName="Fragner" balance="-5000.0" />
 * ]]>
 * </pre>
 * 
 * <pre>
 * public class Account
 * {
 *   private String lastName;
 *   private double balance;
 * 
 * 
 *   public void setLastName(String lastName)
 *   {
 *     this.lastName = lastName;
 *   }
 * 
 * 
 *   public void setBalance(double balance)
 *   {
 *     this.balance = balance;
 *   }
 * }
 * </pre>
 * 
 * 
 * </p>
 * 
 * <p>
 * The Method {@link #getRecorder(Class)} can be used to define a WHERE
 * condition using a method call recording mechanism (see also the first example
 * below). First the programmer must call the desired method on the returned
 * proxy object. This method call is recorded by JaQLib. When JaqLib evaluates
 * the WHERE condition this method call is replayed on every selected element.
 * The result of this method call is then evaluated against the specified
 * condition.
 * </p>
 * <p>
 * Note that every query to a XML file results in reading and parsing the XML
 * file by default. If you want to improve performance then look at example
 * 'Multiple queries on same file'.
 * </p>
 * <p>
 * Note that the JDK XPath engine is used by default. Jaqlib supports a set of
 * other XPath engines, too. See example 'Using a specific or a custom
 * XPathEngine' for further details.
 * </p>
 * <p>
 * Note that various default values for querying XML files can be set
 * application-wide by using the {@link #DEFAULTS} object.
 * </p>
 * <p>
 * Some words about relative paths to the XML files. By default Jaqlib uses the
 * <b>application working directory</b> as starting point for the XML files
 * paths. This functionality is represented by the {@link FileResource} class.
 * If you want to use the classpath as starting point you have to use
 * {@link ClassPathResource}. <br>
 * E.g.
 * 
 * <pre>
 * ClassPathResource cpResource = new ClassPathResource(
 *     &quot;./files/Accounts_Attributes.xml&quot;);
 * FileResource fResource = new FileResource(
 *     &quot;./JaqlibExample.jar!/files/Accounts_Attributes.xml&quot;);
 * 
 * // cpResource and fResoure point to the same XML file
 * // queries access the same XML file
 * 
 * List&lt;? extends Account&gt; cpAccounts = Jaqlib.XML.select(AccountImpl.class)
 *     .fromAttributes(cpResource).where(&quot;/bank/accounts/*&quot;).asList();
 * List&lt;? extends Account&gt; fAccounts = Jaqlib.XML.select(AccountImpl.class)
 *     .fromAttributes(fResource).where(&quot;/bank/accounts/*&quot;).asList();
 * </pre>
 * 
 * </p>
 * <p>
 * This class is thread-safe.
 * <p>
 * <h2>Usage examples</h2><br>
 * The examples below use following XML files:<br>
 * <i>Accounts_Elements.xml:</i>
 * 
 * <pre>
 * <![CDATA[
 * <bank>
 *   <accounts>
 *     <account>
 *       <lastName>huber</lastName>
 *       <firstName>sepp</firstName>
 *       <balance>5000</balance>
 *       <creditRating>GOOD</creditRating>
 *       <department>linz</department>
 *     </account>
 *     <account>
 *       <lastName>maier</lastName>
 *       <firstName>franz</firstName>
 *       <balance>2000</balance>
 *       <creditRating>POOR</creditRating>
 *       <department>wien</department>
 *     </account>
 *   </accounts>
 * </bank>
 * ]]>
 * 
 * <i>Accounts_Attributes.xml:</i>
 * 
 * <pre>
 * <bank>
 *   <accounts>
 *     <account lastName="huber" firstName="sepp" balance="5000" creditRating="GOOD" department="linz" />
 *     <account lastName="maier" firstName="franz" balance="2000" creditRating="POOR" department="wien" />
 *   </accounts>
 * </bank>
 * </pre>
 * 
 * </pre>
 * 
 * <h3>Selecting Java objects using XML elements (default)</h3>
 * 
 * <pre>
 * // using the method call mechanism for constraining the query result
 * Account account = Jaqlib.XML.getRecorder(Account.class);
 * List&lt;? extends Account&gt; accountsGreater500 = Jaqlib.XML.select(
 *     AccountImpl.class).from(&quot;Accounts.xml&quot;).where(&quot;/bank/accounts/*&quot;).andCall(
 *     account.getBalance()).isGreaterThan(500.0).asList();
 * </pre>
 * 
 * Note: the <tt>fromElements()</tt> method does the same as the <tt>from()</tt>
 * method. For using attributes instead of elements see the next example.
 * 
 * <h3>Selecting Java objects using XML attributes</h3>
 * 
 * <pre>
 * List&lt;? extends Account&gt; accounts = Jaqlib.XML.select(AccountImpl.class)
 *     .fromAttributes(&quot;Accounts_Attributes.xml&quot;).where(&quot;/bank/accounts/*&quot;)
 *     .asList();
 * </pre>
 * 
 * <h3>Multiple queries on same file</h3>
 * The datasource should be used when multiple queries are run against one XML
 * file. With this datasource the XML file is read and parsed only once for
 * multiple queries.
 * 
 * <pre>
 * // create data source for caching XML file
 * XmlSelectDataSource ds = new XmlSelectDataSource(&quot;Accounts.xml&quot;);
 * ds.setAutoClose(false);
 * 
 * try
 * {
 *   // create recorder for contraining the query
 *   Account account = Jaqlib.XML.getRecorder(Account.class);
 * 
 *   // execute first query against XML file
 *   List&lt;? extends Account&gt; allAccounts = Jaqlib.XML.select(AccountImpl.class)
 *       .fromElements(ds).where(&quot;/bank/accounts/*&quot;).asList();
 * 
 *   // execute second query against XML file
 *   List&lt;? extends Account&gt; accountsGreater500 = Jaqlib.XML.select(
 *       AccountImpl.class).from(ds).where(&quot;/bank/accounts/*&quot;).andCall(
 *       account.getBalance()).isGreaterThan(500.0).asList();
 * }
 * finally
 * {
 *   // data source must be closed manually
 *   ds.close();
 * }
 * </pre>
 * 
 * 
 * <h3>Using a specific or a custom {@link XPathEngine}</h3>
 * Jaqlib comes with a set of the most common XPath engines:
 * <ul>
 * <li>the build in JDK XPath engine (default)</li>
 * <li>Jaxen</li>
 * <li>Jaxp</li>
 * <li>Saxon</li>
 * <li>Xalan</li>
 * </ul>
 * You are free to implement your own XPath engine wrapper by implementing the
 * {@link XPathEngine} interface and setting the engine using
 * {@link XmlSelectDataSource}. If you want to use another default XPath engine
 * than the JDK XPath engine then just call
 * {@link XmlDefaults#setXPathEngine(XPathEngine)}.<br>
 * e.g. <tt>Jaqlib.XML.DEFAULTS.setXPathEngine(new JaxenXPathEngine());</tt>.
 * 
 * <pre>
 * // create data source for setting a specific XPath engine
 * XmlSelectDataSource ds = new XmlSelectDataSource(&quot;Accounts.xml&quot;);
 * ds.setXPathEngine(new XalanXPathEngine());
 * 
 * // execute query
 * List&lt;? extends Account&gt; allAccounts = Jaqlib.XML.select(AccountImpl.class)
 *     .fromElements(ds).where(&quot;/bank/accounts/*&quot;).asList();
 * </pre>
 * 
 * <h3>Using a custom bean mapping</h3> By default the XML element or attribute
 * names must exactly match the Jave bean field names. This mapping behavior can
 * be changed by using the {@link BeanMapping} class. It holds the information
 * how to map the source XML fields to the target Java bean fields.
 * 
 * <pre>
 * // rename field 'lastName' and remove field 'department'
 * BeanMapping&lt;Account&gt; mapping = new BeanMapping&lt;Account&gt;(Account.class);
 * mapping.getChildField(&quot;lastName&quot;).setSourceName(&quot;last_name&quot;);
 * mapping.removeChildColumn(&quot;department&quot;);
 * 
 * List&lt;? extends Account&gt; accounts = Jaqlib.XML.select(mapping).from(
 *     &quot;Accounts.xml&quot;).where(&quot;/bank/accounts/*&quot;).asList();
 * </pre>
 * 
 * If you want to take full control of the mapping functionality you can use the
 * {@link BeanMappingStrategy} interface. It can be set into the {@link BeanMapping}
 * object or can be set application-wide by using the {@link Defaults} class.
 * 
 * @author Werner Fragner
 */
public class XmlQueryBuilder extends AbstractQueryBuilder
{

  /**
   * Contains the application wide default values for the XML query builder.
   * Default values can be specified for the used {@link XPathEngine} or the
   * used XML namespaces.
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
