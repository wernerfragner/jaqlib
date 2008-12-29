package org.jaqlib.query.db;

import org.jaqlib.db.BeanDbSelectResult;
import org.jaqlib.db.DbSelect;
import org.jaqlib.db.PrimitiveDbSelectResult;
import org.jaqlib.query.AbstractQueryBuilder;
import org.jaqlib.query.FromClause;
import org.jaqlib.query.ReflectiveWhereCondition;
import org.jaqlib.query.WhereClause;
import org.jaqlib.query.WhereCondition;
import org.jaqlib.util.Assert;

/**
 * <p>
 * The main entry point of JaQLib for database query support. It provides
 * methods for building queries ( {@link #select(PrimitiveDbSelectResult),
 * #select(BeanDbSelectResult) }) and adapting the query building process (
 * {@link #setClassLoader(ClassLoader)} ).</br> The Method
 * {@link #getMethodCallRecorder(Class)} can be used to define a WHERE condition
 * where a return value of method call is compared to an other value (see also
 * the first example below).
 * </p>
 * <p>
 * <b>Usage examples:</b><br>
 * <i>Example with method call recording:</i>
 * 
 * <pre>
 * // create a 'dummy' object for recording a method call for the WHERE clause
 * Account account = DatabaseQB.getMethodCallRecorder(Account.class);
 * 
 * // select all accounts with a balance greater than 5000
 * String sql = &quot;SELECT lastname, firstname, creditrating, balance FROM APP.ACCOUNT&quot;;
 * DbSelect dataSource = Db.getSelect(getDataSource(), sql);
 * BeanDbSelectResult&lt;AccountImpl&gt; resultDefinition = Db
 *     .getBeanResult(AccountImpl.class);
 * List&lt;Account&gt; results = DatabaseQB.select(resultDefinition).from(dataSource)
 *     .where(account.getBalance()).isGreaterThan(5000).asList();
 * </pre>
 * 
 * <i>Example with user-defined WHERE conditions:</i>
 * 
 * <pre>
 * // create condition for negative balances
 * WhereCondition deptCondition = new WhereCondition() {
 * 
 *   public boolean evaluate(Account account) {
 *     return (account.getBalance() &lt; 0);
 *   }
 * 
 * };
 * 
 * // create condition for accounts with poor credit rating
 * WhereCondition ratingCondition = new WhereCondition() {
 * 
 *   public boolean evaluate(Account account) {
 *     return (account.getCreditRating() == CreditRating.POOR);
 *   }
 * }
 * 
 * // execute query with these conditions 
 * Account.class
 * List&lt;Account&gt; highRiskAccounts = QB.select().from(accounts)
 *     .where(deptCondition).and(ratingCondition).asList();
 * </pre>
 * 
 * <i>Example for filtering out null elements:</i>
 * 
 * <pre>
 * List&lt;Account&gt; notNullAccounts = QB.select(Account.class).from(accounts).where()
 *     .element().isNotNull().asList();
 * </pre>
 * 
 * <i>Example for using {@link Comparable} elements:</i>
 * 
 * <pre>
 * // Account implements the Comparable interface; the balance field is used for comparing two accounts
 * Account spec = new Account();
 * account.setBalance(5000);
 * 
 * List&lt;Account&gt; result = QB.select(Account.class).from(accounts).where()
 *     .element().isSmallerThan(spec).asList();
 * </pre>
 * 
 * <i>Example using a Map as result:</i>
 * 
 *<pre>
 * Account account = QB.getMethodCallRecorder(Account.class);
 * Map&lt;String, Account&gt; results = QB.select(Account.class).from(accounts).asMap(
 *     account.getId());
 * </pre>
 * 
 * </p>
 * 
 * @author Werner Fragner
 */
public class DatabaseQueryBuilder extends AbstractQueryBuilder
{

  private final DatabaseQBProperties properties;


  /**
   * Default constructor that initializes this class with the default
   * properties.
   */
  public DatabaseQueryBuilder()
  {
    this(new DatabaseQBProperties());
  }


  /**
   * Constructor for specifying user-defined properties.
   * 
   * @param properties a not null properties object.
   */
  public DatabaseQueryBuilder(DatabaseQBProperties properties)
  {
    this.properties = Assert.notNull(properties);
  }


  /**
   * <p>
   * Selects one column of a given database SELECT statement. The SELECT
   * statement that should be used must be specified in the returned
   * {@link FromClause}. The {@link FromClause} hereby returns a
   * {@link WhereClause} that can be used to specify an arbitrary WHERE
   * condition. This WHERE condition supports AND and OR connectors, the
   * evaluation of user-defined {@link WhereCondition}s and user-defined
   * {@link ReflectiveWhereCondition}s.
   * </p>
   * <p>
   * <b>NOTE: the WHERE condition is not executed at database-side but at Java
   * side. Avoid executing SELECT statements with lots of data and then
   * constraining it with the WHERE functionality of JaqLib!</b>.
   * </p>
   * 
   * @param <T> the result element type.
   * @param resultDefinition an object defining the desired result.
   * @return the FROM clause to specify the database SELECT statement for the
   *         query.
   */
  public <T> FromClause<T, DbSelect> select(
      PrimitiveDbSelectResult<T> resultDefinition)
  {
    return this.<T> createQuery().createFromClause(resultDefinition);
  }


  /**
   * <p>
   * Uses a given database SELECT statement to fill a user-defined Java bean.
   * The SELECT statement that should be used must be specified in the returned
   * {@link FromClause}. The {@link FromClause} hereby returns a
   * {@link WhereClause} that can be used to specify an arbitrary WHERE
   * condition. This WHERE condition supports AND and OR connectors, the
   * evaluation of user-defined {@link WhereCondition}s and user-defined
   * conditions using a method call recording mechanism (see examples and
   * {@link #getMethodCallRecorder(Class)} for further details).
   * </p>
   * <p>
   * <b>NOTE: the WHERE condition is not executed at database-side but at Java
   * side. Avoid executing SELECT statements with lots of data and then
   * constraining it with the WHERE functionality of JaqLib!</b>.
   * </p>
   * 
   * @param <T> the result element type.
   * @param resultDefinition an object defining the desired result bean
   *          mappings.
   * @return the FROM clause to specify the database SELECT statement for the
   *         query.
   */
  public <T> FromClause<T, DbSelect> select(
      BeanDbSelectResult<T> resultDefinition)
  {
    return this.<T> createQuery().createFromClause(resultDefinition);
  }


  private <T> DatabaseQuery<T> createQuery()
  {
    return new DatabaseQuery<T>(getMethodCallRecorder(), properties);
  }

}
