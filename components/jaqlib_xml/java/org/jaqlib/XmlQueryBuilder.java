package org.jaqlib;

import org.jaqlib.core.AbstractQueryBuilder;
import org.jaqlib.core.WhereCondition;
import org.jaqlib.core.bean.BeanFactory;
import org.jaqlib.core.bean.BeanMapping;
import org.jaqlib.core.bean.BeanMappingStrategy;
import org.jaqlib.core.bean.JavaTypeHandler;
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
 * This class is the main entry point of JaQLib for XML query support. It
 * provides following methods for building queries:
 * <ul>
 * <li>{@link #select(Class)}</li>
 * <li>{@link #select(BeanMapping)}</li>
 * </ul>
 * </p>
 * <p>
 * The mapping between XML data and Java bean fields is done using XML elements
 * or XML attributes. For example, if you have a XML element 'account' with the
 * attributes 'lastName' and 'balance' then you would have to have a class with
 * the fields 'lastName' and 'balance'. The same applies when you use nested XML
 * elements instead of XML attributes.<br>
 * Additionally you can select, for example, only the last names of all account,
 * too.
 * </p>
 * 
 * <pre>
 * {@code
 * <account lastName="Fragner" balance="-5000.0" />
 * }
 * </pre>
 * 
 * or
 * 
 * <pre>
 * {@code
 * <account>
 *   <lastName>Fragner</lastName>
 *   <balance>-5000.0</balance>
 * </account>
 * }
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
 * <h2>Important issues</h2>
 * <ul>
 * <li>
 * Every query to a XML file results in reading and parsing the XML file by
 * default. If you want to improve performance then look at example 'Multiple
 * queries on same XML file'.</li>
 * <li>
 * The WHERE condition is not executed by the XPath engine but at Java side.
 * Avoid executing XPath expressions with lots of data and then constraining it
 * with the WHERE functionality of JaqLib!</li>
 * <li>
 * By default the JDK XPath engine is used. Jaqlib supports a set of other XPath
 * engines, too. See example 'Using a specific or a custom XPathEngine' for
 * further details.</li>
 * <li>
 * The mapping between XML attributes and elements to Java bean fields can be
 * adapted by using a {@link BeanMapping} object. See example 'Define a custom
 * bean mapping' for further details.</li>
 * <li>
 * Various default values for querying XML files can be set application-wide by
 * using the {@link #DEFAULTS} object.</li>
 * <li>
 * Jaqlib also supports nested Java beans hierarchies. That means that a Java
 * bean can have other Java beans as fields. The XML structure must reflect this
 * hierarchy, of course, in order that Jaqlib can do the right mapping and bean
 * instantiation.</li>
 * <li>This class is thread-safe.</li>
 * </ul>
 * 
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
 *     .from(cpResource).where(&quot;/bank/accounts/*&quot;).asList();
 * List&lt;? extends Account&gt; fAccounts = Jaqlib.XML.select(AccountImpl.class)
 *     .from(fResource).where(&quot;/bank/accounts/*&quot;).asList();
 * </pre>
 * 
 * </p>
 * 
 * <h2>Usage examples</h2> The examples below use following XML files:<br>
 * <i>Accounts_Attributes.xml:</i>
 * 
 * <pre>
 * {@code
 * <bank>
 *   <accounts>
 *     <account lastName="huber" firstName="sepp" balance="5000" creditRating="GOOD" department="linz" />
 *     <account lastName="maier" firstName="franz" balance="2000" creditRating="POOR" department="wien" />
 *   </accounts>
 * </bank>
 * }
 * </pre>
 * 
 * <i>Accounts_Elements.xml:</i>
 * 
 * <pre>
 * {@code
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
 * }
 * </pre>
 * 
 * <h3>Selecting Java objects using XML attributes (default)</h3>
 * 
 * <pre>
 * List&lt;? extends Account&gt; accounts = Jaqlib.XML.select(AccountImpl.class)
 *     .from(&quot;Accounts_Attributes.xml&quot;).where(&quot;/bank/accounts/*&quot;).asList();
 * </pre>
 * 
 * or (does the same)
 * 
 * <pre>
 * List&lt;? extends Account&gt; accounts = Jaqlib.XML.select(AccountImpl.class)
 *     .fromAttributes(&quot;Accounts_Attributes.xml&quot;).where(&quot;/bank/accounts/*&quot;)
 *     .asList();
 * </pre>
 * 
 * <h3>Selecting Java objects using XML elements</h3>
 * 
 * <pre>
 * List&lt;? extends Account&gt; accounts = Jaqlib.XML.select(AccountImpl.class)
 *     .fromElements(&quot;Accounts_Elements.xml&quot;).where(&quot;/bank/accounts/*&quot;).asList();
 * </pre>
 * 
 * <h3>Selecting primitive values</h3> When you want to query XML for primitive
 * values (e.g. String, int, ...) you can simply write the following:
 * 
 * <pre>
 * List&lt;String&gt; lastNames = Jaqlib.XML.select(String.class).from(&quot;Accounts.xml&quot;)
 *     .where(&quot;/bank/accounts/account/@lastName&quot;).asList();
 * </pre>
 * 
 * If you want to perform custom type conversions (see also example further
 * below) you must register the according {@link JavaTypeHandler}
 * application-wide at the {@link XmlDefaults} class:
 * 
 * <pre>
 * // register java type handler
 * Jaqlib.XML.DEFAULTS
 *     .registerJavaTypeHandler(new CreditRatingStringTypeHandler());
 * 
 * // execute query
 * List&lt;CreditRating&gt; ratings = Jaqlib.XML.select(CreditRating.class)
 *     .from(&quot;Accounts.xml&quot;).where(&quot;//@creditRating&quot;).asList();
 * </pre>
 * 
 * <h3>Selecting complex Java objects</h3> Jaqlib also support nested bean
 * hierarchies. That means that one Bean has other beans as fields. In the
 * example below we have the Bean 'Account' that has a collection of other beans
 * named 'Transaction'. The according XML file could like like this:
 * 
 * <pre>
 * {@code
 * <account lastName="Fragner" balance="-5000.0" >
 *   <transaction id="1" value="1349.30" />
 *   <transaction id="2" value="-400.00" />
 * </account>
 * }
 * </pre>
 * 
 * or
 * 
 * <pre>
 * {@code
 * <account lastName="Fragner" balance="-5000.0" >
 *   <transactions>
 *     <transaction id="1" value="1349.30" />
 *     <transaction id="2" value="-400.00" />
 *   </transactions>
 * </account>
 * }
 * </pre>
 * 
 * The 'Account' class must have a field 'transactions' or 'transactionList' in
 * order that Jaqlib can automatically do the mapping. Jaqlib requires a
 * collection field that ends with 's' or 'List' (if that's not possible, see
 * next example). The 'Transaction' class has two fields 'id' and 'value'. We
 * can select this account by using following code:
 * 
 * <pre>
 * List&lt;? extends Account&gt; accounts = Jaqlib.XML.select(Account.class)
 *     .from(&quot;Accounts.xml&quot;).where(&quot;/bank/accounts/*&quot;).asList();
 * </pre>
 * 
 * If the collection field does not end with 's' or 'List' you can define your
 * own collection mapping logic:
 * 
 * <pre>
 * BeanMapping&lt;Account&gt; mapping = new BeanMapping&lt;Account&gt;(Account.class);
 * 
 * // set a custom source name for the transaction collection
 * mapping.getCollectionField(&quot;transactions&quot;).setSourceName(&quot;accTransactions&quot;);
 * 
 * // set a custom source name for the elements of the transaction collection
 * mapping.getCollectionField(&quot;transactions&quot;).setElementSourceName(
 *     &quot;accTransaction&quot;);
 * 
 * // execute query
 * List&lt;? extends Account&gt; accounts = Jaqlib.XML.select(Account.class)
 *     .from(&quot;Accounts.xml&quot;).where(&quot;/bank/accounts/*&quot;).asList();
 * </pre>
 * 
 * Finally, you can also map collections with primitive values:
 * 
 * <pre>
 * {@code
 * <account lastName="Fragner" balance="-5000.0" >
 *   <transactions>
 *     <transaction>1349.30</transaction>
 *     <transaction>-400.00"</transaction>
 *   </transactions>
 * </account>
 * }
 * </pre>
 * 
 * <pre>
 * public class AccountImpl
 * {
 *   private List&lt;Double&gt; transactions = new ArrayList&lt;Double&gt;();
 * 
 * 
 *   public List&lt;Double&gt; getTransactions()
 *   {
 *     return transactions;
 *   }
 * 
 * 
 *   public void setTransactions(List&lt;Double&gt; tx)
 *   {
 *     this.transactions = tx;
 *   }
 * 
 *   // remaining code omitted
 * }
 * </pre>
 * 
 * Jaqlib determines at runtime the element type of the collection and tries to
 * convert the string values of the XML elements to the according element type
 * (also using the {@link JavaTypeHandler} mechanism - see also examples further
 * below). Note that only XML elements are supported. XML attributes cannot be
 * specified multiple times on an element - so they cannot be used for mapping
 * primitive XML values to Java collection values.
 * 
 * <h3>Constraining the result</h3> There are different ways how to constrain
 * the returned query result. Jaqlib provides an API for specifying WHERE
 * clauses. You can use WHERE clauses in three ways:
 * <ul>
 * <li>Method call recording mechanism</li>
 * <li>Custom where condition code</li>
 * <li>Simple comparison methods</li>
 * </ul>
 * 
 * <p>
 * <b>Method call recording mechanism</b><br>
 * By using the Method {@link #getRecorder(Class)} a recorder object is created
 * (= a JDK dynamic proxy). First the programmer must call the desired method on
 * the returned recorder object. This method call is recorded by JaQLib. When
 * JaqLib evaluates the WHERE condition this method call is replayed on every
 * selected element. The result of this method call is then evaluated against
 * the condition that is specified after the WHERE clause.
 * </p>
 * 
 * <pre>
 * // get recorder object
 * Account account = Jaqlib.XML.getRecorder(Account.class);
 * 
 * // execute query
 * List&lt;? extends Account&gt; accountsGreater500 = Jaqlib.XML
 *     .select(AccountImpl.class).from(&quot;Accounts_Attributes.xml&quot;)
 *     .where(&quot;/bank/accounts/*&quot;).andCall(account.getBalance())
 *     .isGreaterThan(500.0).asList();
 * </pre>
 * 
 * <p>
 * In the example above the method call <tt>account.getBalance()</tt> is
 * recorded by Jaqlib. When Jaqlib executes the query this method is called on
 * every selected object. Each result of this method call is then evaluated
 * according to the given condition <tt>isGreaterThan(500.0)</tt>. Only the
 * selected elements that match this condition are returned.
 * </p>
 * 
 * <p>
 * <b>Custom where condition code</b><br>
 * By implementing the interface {@link WhereCondition} you can define your own
 * condition code. This alternative gives you most flexibility but is somewhat
 * cumbersome to implement (see example below).
 * </p>
 * 
 * <pre>
 * // create custom WHERE condition
 * WhereCondition&lt;AccountImpl&gt; myCondition = new WhereCondition&lt;AccountImpl&gt;()
 * {
 * 
 *   &#064;Override
 *   public boolean evaluate(AccountImpl element)
 *   {
 *     if (element == null)
 *       return false;
 *     return element.getBalance() &gt; 500;
 *   }
 * };
 * 
 * // execute query
 * List&lt;? extends Account&gt; accountsGreater500 = Jaqlib.XML
 *     .select(AccountImpl.class).from(&quot;Accounts_Attributes.xml&quot;)
 *     .where(&quot;/bank/accounts/*&quot;).and(myCondition).asList();
 * </pre>
 * 
 * <p>
 * <b>Simple comparison methods</b><br>
 * There are some comparison methods that can be used for criteria matching (see
 * <a href=
 * "http://java.sun.com/blueprints/corej2eepatterns/Patterns/DataAccessObject.html"
 * >DataAccessObject<a>) or for matching primitive types.
 * </p>
 * 
 * <pre>
 * long accountId = 15;
 * AccountImpl criteria = new AccountImpl();
 * criteria.setId(accountId);
 * 
 * Account account15 = Jaqlib.XML.select(AccountImpl.class)
 *     .from(&quot;Accounts_Attributes.xml&quot;).where(&quot;/bank/accounts/*&quot;).andElement()
 *     .isEqual(criteria).uniqueResult();
 * </pre>
 * 
 * For example, you can use this mechanism also for filtering {@link Comparable}
 * elements:</i>
 * 
 * <pre>
 * // Account implements the Comparable interface; the balance field is used
 * // for comparing two accounts
 * AccountImpl criteria = new AccountImpl();
 * criteria.setBalance(5000.0);
 * 
 * List&lt;AccountImpl&gt; result = Jaqlib.XML.select(AccountImpl.class)
 *     .from(&quot;Accounts.xml&quot;).where(&quot;/bank/accounts/*&quot;).andElement()
 *     .isSmallerThan(criteria).asList();
 * </pre>
 * 
 * <h3>Multiple queries on same file</h3> When multiple queries are run against
 * one XML file the {@link XmlSelectDataSource} should be used. With this
 * datasource the XML file is read and parsed only once for multiple queries.
 * The {@link XmlSelectDataSource} must be configured with <tt>autoClose</tt> to
 * false. And additionally the datasource must be closed manually by calling
 * {@link XmlSelectDataSource#close()}.
 * 
 * <pre>
 * // create data source for caching XML file
 * XmlSelectDataSource ds = new XmlSelectDataSource(&quot;Accounts_Attributes.xml&quot;);
 * ds.setAutoClose(false);
 * 
 * try
 * {
 *   // create recorder for contraining the query
 *   Account account = Jaqlib.XML.getRecorder(Account.class);
 * 
 *   // execute first query against XML file
 *   List&lt;? extends Account&gt; allAccounts = Jaqlib.XML.select(AccountImpl.class)
 *       .from(ds).where(&quot;/bank/accounts/*&quot;).asList();
 * 
 *   // execute second query against XML file
 *   List&lt;? extends Account&gt; accountsGreater500 = Jaqlib.XML
 *       .select(AccountImpl.class).from(ds).where(&quot;/bank/accounts/*&quot;)
 *       .andCall(account.getBalance()).isGreaterThan(500.0).asList();
 * }
 * finally
 * {
 *   // data source must be closed manually
 *   ds.close();
 * }
 * </pre>
 * 
 * 
 * <h3>Using a specific or a custom {@link XPathEngine}</h3> Jaqlib comes with a
 * set of the most common XPath engines:
 * <ul>
 * <li>the JDK XPath engine (default)</li>
 * <li><a href="http://jaxen.codehaus.org/">Jaxen</a></li>
 * <li><a href="http://saxon.sourceforge.net/">Saxon</a></li>
 * <li><a href="http://xalan.apache.org/">Xalan</a></li>
 * <li><a href="http://en.wikipedia.org/wiki/Java_API_for_XML_Processing" >JAXP
 * (on Wikipedia)</a></li>
 * </ul>
 * You are free to implement your own XPath engine wrapper by implementing the
 * {@link XPathEngine} interface and setting the engine using
 * {@link XmlSelectDataSource#setXPathEngine(XPathEngine)}.
 * 
 * <pre>
 * // create data source for setting a specific XPath engine
 * XmlSelectDataSource ds = new XmlSelectDataSource(&quot;Accounts_Attributes.xml&quot;);
 * ds.setXPathEngine(new XalanXPathEngine());
 * 
 * // execute query
 * List&lt;? extends Account&gt; allAccounts = Jaqlib.XML.select(AccountImpl.class)
 *     .from(ds).where(&quot;/bank/accounts/*&quot;).asList();
 * </pre>
 * 
 * Alternatively you can set the default XPath engine application wide:
 * 
 * <pre>
 * Jaqlib.XML.DEFAULTS.setXPathEngine(new JaxenXPathEngine());
 * </pre>
 * 
 * <h3>Define a custom bean mapping</h3> By default the XML element or attribute
 * names must exactly match the Jave bean field names. This mapping behavior can
 * be changed by using the {@link BeanMapping} class. It holds the information
 * how to map the source XML elements/attributes to the target Java bean fields.
 * 
 * <pre>
 * // rename field 'lastName' and remove field 'department'
 * BeanMapping&lt;Account&gt; mapping = new BeanMapping&lt;Account&gt;(Account.class);
 * mapping.getField(&quot;lastName&quot;).setSourceName(&quot;last_name&quot;);
 * mapping.removeField(&quot;department&quot;);
 * 
 * List&lt;? extends Account&gt; accounts = Jaqlib.XML.select(mapping)
 *     .from(&quot;Accounts_Attributes.xml&quot;).where(&quot;/bank/accounts/*&quot;).asList();
 * </pre>
 * 
 * If you want to take full control of the mapping functionality you can use the
 * {@link BeanMappingStrategy} interface. It can be set into the
 * {@link BeanMapping} object or can be set application-wide by calling
 * {@link XmlDefaults#setBeanMappingStrategy(BeanMappingStrategy)}.
 * 
 * <h3>Using a custom Java type handler</h3> In some cases the default mapping
 * logic cannot be applied for certain fields. In that case a custom
 * {@link JavaTypeHandler} can be used. The type handler performs conversion
 * from the source data type to the Java bean field type. With this mechanism,
 * for example, a set of string values can be converted to Java enumeration
 * values. The example below shows such a conversion of string values to the
 * <tt>CreditRating</tt> enumeration.
 * 
 * <pre>
 * // set a custom type handler for the 'creditRating' field
 * BeanMapping&lt;Account&gt; mapping = new BeanMapping&lt;Account&gt;(Account.class);
 * mapping.getField(&quot;creditRating&quot;).setTypeHandler(
 *     new CreditRatingStringTypeHandler());
 * 
 * // execute query
 * List&lt;? extends Account&gt; accounts = XmlQB.select(mapping)
 *     .from(&quot;Accounts_Attributes.xml&quot;).where(&quot;/bank/accounts/*&quot;).asList();
 * </pre>
 * 
 * Alternatively the type handler can be set application-wide:
 * 
 * <pre>
 * Jaqlib.XML.DEFAULTS
 *     .registerJavaTypeHandler(new CreditRatingStringTypeHandler());
 * </pre>
 * 
 * The custom Java type handler:
 * 
 * <pre>
 * public class CreditRatingTypeHandler extends AbstractJavaTypeHandler
 * {
 * 
 *   &#064;Override
 *   public void addSupportedTypes(List&lt;Class&lt;?&gt;&gt; types)
 *   {
 *     types.add(CreditRating.class);
 *   }
 * 
 * 
 *   public Object convert(Object value)
 *   {
 *     if (value instanceof String)
 *     {
 *       String str = (String) value;
 *       if (str.equals(CreditRating.POOR.getName()))
 *       {
 *         return CreditRating.POOR;
 *       }
 *       else if (str.equals(CreditRating.GOOD.getName()))
 *       {
 *         return CreditRating.GOOD;
 *       }
 *     }
 * 
 *     throw handleIllegalInputValue(value, CreditRating.class);
 *   }
 * 
 * }
 * </pre>
 * 
 * <h3>Using XML namespaces</h3> If XML files use namespaces for the XML
 * elements or attributes you must register these namespaces with the XPath
 * engine. See the examples below how this can be achieved.
 * 
 * <pre>
 * {@code
 * <document xmlns:bankapp="http://werner.fragner.org/jaqlib/bankapp" >
 *   <bank>
 *     <bankapp:accounts>
 *       <bankapp:account bankapp:lastName="huber" firstName="sepp" balance="5000" creditRating="GOOD" department="linz">
 *         <transactions>
 *           <transaction id="1" amount="100"/>
 *           <transaction id="2" amount="-100"/>
 *         </transactions>
 *       </bankapp:account>
 *     </bankapp:accounts>
 *   </bank>
 * </document>
 * }
 * </pre>
 * 
 * <pre>
 * // register a XML namespace
 * XmlSelectDataSource ds = new XmlSelectDataSource(&quot;Accounts_namespaces.xml&quot;);
 * ds.addNamespace(&quot;bankapp&quot;, &quot;http://werner.fragner.org/jaqlib/bankapp&quot;);
 * 
 * List&lt;? extends Account&gt; accounts = Jaqlib.XML.select(AccountImpl.class)
 *     .from(ds).where(&quot;/bank/accounts/*&quot;).asList();
 * </pre>
 * 
 * Alternatively the namespace can be registered application-wide:
 * 
 * <pre>
 * Jaqlib.XML.DEFAULTS.addNamespace(&quot;bankapp&quot;,
 *     &quot;http://werner.fragner.org/jaqlib/bankapp&quot;);
 * </pre>
 * 
 * @see XmlDefaults
 * @see XmlQB
 * @author Werner Fragner
 */
public class XmlQueryBuilder extends AbstractQueryBuilder
{

  /**
   * Contains the application wide default values for the XML query
   * functionality. Default values can be specified for the used
   * {@link XPathEngine} or the used XML namespaces.
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
   *          A {@link BeanMapping} can be instantiated with the <tt>new</tt>
   *          operator or by using {@link XmlQB#getDefaultBeanMapping(Class)}
   *          and {@link XmlQB#getBeanMapping(BeanMappingStrategy, Class)}. This
   *          {@link BeanMapping} can be used in the
   *          {@link #select(BeanMapping)} method.
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
   * mapping between XPath expression results and Java bean fields. This mapping
   * can defined with a {@link BeanMapping} instance. A {@link BeanMapping} can
   * be instantiated with the <tt>new</tt> operator or by using
   * {@link XmlQB#getDefaultBeanMapping(Class)} and
   * {@link XmlQB#getBeanMapping(BeanMappingStrategy, Class)}.
   * 
   * @param <T> the result bean type.
   * @param beanMapping a bean definition that holds information how to map the
   *          result of the query to a Java bean.
   * @return the FROM clause to specify the input XML file for the query.
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
