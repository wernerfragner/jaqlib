package org.jaqlib;

import org.jaqlib.core.Task;
import org.jaqlib.core.WhereCondition;
import org.jaqlib.core.bean.AbstractJavaTypeHandler;
import org.jaqlib.core.bean.BeanMapping;
import org.jaqlib.db.DbDeleteDataSource;
import org.jaqlib.db.DbInsertDataSource;
import org.jaqlib.db.DbSelectDataSource;
import org.jaqlib.db.DbUpdateDataSource;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DatabaseQBExamples
{

  private DbSelectDataSource accounts;


  public void setup()
  {
    String sql = "SELECT lname AS lastname, fname AS firstname, creditrating, balance FROM APP.ACCOUNT";
    accounts = new DbSelectDataSource(getJdbcDataSource(), sql);
  }


  private DataSource getJdbcDataSource()
  {
    // TODO Auto-generated method stub
    return null;
  }


  public void selectPrimitve()
  {
    String sql = "select count(*) from accounts";
    DbSelectDataSource dataSource = new DbSelectDataSource(getJdbcDataSource(),
        sql);

    int nrRecords = Jaqlib.DB.select(Integer.class).from(dataSource)
        .uniqueResult();
  }


  public void selectBean()
  {
    List<? extends Account> result = Jaqlib.DB.select(AccountImpl.class)
        .from(accounts).asList();
  }


  public void selectMethodRecordMechanism()
  {
    // get recorder object
    Account account = Jaqlib.DB.getRecorder(Account.class);

    // select all accounts with a balance greater than 500
    List<? extends Account> results = Jaqlib.DB.select(AccountImpl.class)
        .from(accounts).whereCall(account.getBalance()).isGreaterThan(500.0)
        .asList();
  }


  public void customWhereCondition()
  {
    // create custom WHERE condition
    WhereCondition<AccountImpl> myCondition = new WhereCondition<AccountImpl>()
    {

      @Override
      public boolean evaluate(AccountImpl element)
      {
        if (element == null) return false;
        return element.getBalance() > 500;
      }
    };

    // execute query
    List<? extends Account> accountsGreater500 = Jaqlib.DB
        .select(AccountImpl.class).from(accounts).where(myCondition).asList();
  }


  public void simpleComparisonMethod()
  {
    AccountImpl criteria = new AccountImpl();
    criteria.setId((long) 15);

    Account account15 = Jaqlib.DB.select(AccountImpl.class).from(accounts)
        .whereElement().isEqual(criteria).uniqueResult();
  }


  public void compareable()
  {
    // Account implements the Comparable interface; the balance field is used
    // for comparing two accounts
    AccountImpl criteria = new AccountImpl();
    criteria.setBalance(5000.0);

    List<AccountImpl> result = Jaqlib.DB.select(AccountImpl.class)
        .from(accounts).where().element().isSmallerThan(criteria).asList();
  }


  public void mapResult()
  {
    Account recorder = Jaqlib.DB.getRecorder(Account.class);
    Map<Long, AccountImpl> results = Jaqlib.DB.select(AccountImpl.class)
        .from(accounts).asMap(recorder.getId());
  }


  public void setResult()
  {
    Set<AccountImpl> notNullAccounts = Jaqlib.DB.select(AccountImpl.class)
        .from(accounts).whereElement().isNotNull().asSet();
  }


  public void listResult()
  {
    List<AccountImpl> notNullAccounts = Jaqlib.DB.select(AccountImpl.class)
        .from(accounts).whereElement().isNotNull().asList();
  }


  public void uniqueResult()
  {
    Account recorder = Jaqlib.DB.getRecorder(Account.class);
    Account result = Jaqlib.DB.select(AccountImpl.class).from(accounts)
        .whereCall(recorder.getId()).isEqual((long) 5).asUniqueResult();
  }


  public void firstResult()
  {
    Account recorder = Jaqlib.DB.getRecorder(Account.class);
    Account result = Jaqlib.DB.select(AccountImpl.class).from(accounts)
        .whereCall(recorder.getBalance()).isGreaterThan(500.0).asFirstResult();
  }


  public void lastResult()
  {
    Account recorder = Jaqlib.DB.getRecorder(Account.class);
    Account result = Jaqlib.DB.select(AccountImpl.class).from(accounts)
        .whereCall(recorder.getBalance()).isGreaterThan(500.0).asLastResult();
  }


  public void executeTask()
  {
    // create task that should be executed for each element
    Task<Account> task = new Task<Account>()
    {

      @Override
      public void execute(Account account)
      {
        account.sendInfoEmail();
      }

    };
    Jaqlib.DB.select(AccountImpl.class).from(accounts).execute(task);
  }


  public void executeTaskWithResult()
  {
    Task<Account> task = null;

    // create condition for negative balances
    WhereCondition<Account> deptCond = new WhereCondition<Account>()
    {

      @Override
      public boolean evaluate(Account account)
      {
        return (account.getBalance() < 0);
      }

    };

    // execute task only on elements that match the given condition
    Jaqlib.DB.select(AccountImpl.class).from(accounts).where(deptCond)
        .execute(task);

    // or ...
    List<AccountImpl> result = Jaqlib.DB.select(AccountImpl.class)
        .from(accounts).where(deptCond).executeWithResult(task).asList();
  }


  public void preparedStatement()
  {
    String sql = "SELECT lname AS lastname, fname AS firstname, creditrating, balance FROM APP.ACCOUNT WHERE fname = ? AND balance > ?";
    DbSelectDataSource accounts = new DbSelectDataSource(getJdbcDataSource(),
        sql);
    accounts.setAutoClosePreparedStatement(false);

    try
    {
      List<AccountImpl> results1000 = Jaqlib.DB.select(AccountImpl.class)
          .from(accounts).using("werner", 1000).asList();

      List<AccountImpl> results2000 = Jaqlib.DB.select(AccountImpl.class)
          .from(accounts).using("werner", 2000).asList();

      List<AccountImpl> results3000 = Jaqlib.DB.select(AccountImpl.class)
          .from(accounts).using("werner", 3000).asList();
    }
    finally
    {
      // close all used statements (in that case it's only one statement)
      accounts.close();
    }
  }


  public void customJavaTypeHandler()
  {
    // get DbSelectDataSource and BeanMapping
    String sql = "SELECT lname AS lastname, fname AS firstname, creditrating, balance FROM APP.ACCOUNT";
    DbSelectDataSource dataSource = new DbSelectDataSource(getJdbcDataSource(),
        sql);
    BeanMapping<AccountImpl> mapping = new BeanMapping<AccountImpl>(
        AccountImpl.class);

    // register custom type handler for CreditRating bean field
    mapping.registerJavaTypeHandler(new CreditRatingTypeHandler());

    // perform query
    Jaqlib.DB.select(mapping).from(dataSource); // ...
  }

  // custom java type handler that converts Integer values from DB into
  // CreditRating enumerations
  public class CreditRatingTypeHandler extends AbstractJavaTypeHandler
  {
    @Override
    public Object convert(Object value)
    {
      if (value instanceof Integer) return CreditRating.rating((Integer) value);
      else
        throw super.handleIllegalInputValue(value, CreditRating.class);
    }


    @Override
    protected void addSupportedTypes(List<Class<?>> types)
    {
      types.add(CreditRating.class);
    }
  }


  public void customBeanFactory()
  {
    // set bean factory application-wide
    CustomBeanFactory beanFactory = new CustomBeanFactory(new EMailComponent());
    Jaqlib.DB.DEFAULTS.setBeanFactory(beanFactory);

    // set bean factory just for one specific mapping
    BeanMapping<Account> mapping = new BeanMapping<Account>(AccountImpl.class);
    mapping.setFactory(beanFactory);

    // perform select
  }


  public void insertDefaultMapping()
  {
    AccountImpl account = new AccountImpl();
    // fill account with values ...

    String tableName = "ACCOUNT";
    DbInsertDataSource dataSource = new DbInsertDataSource(getJdbcDataSource(),
        tableName);
    int updateCount = Jaqlib.DB.insert(account).into(dataSource)
        .usingDefaultMapping();
  }


  public void insertCustomMapping()
  {
    Account account = new AccountImpl();
    // fill account with values ...

    // create custom bean mapping
    BeanMapping<Account> beanMapping = new BeanMapping<Account>(
        AccountImpl.class);
    beanMapping.removeField("id");
    beanMapping.getField("lastName").setSourceName("lName");

    // insert account using the custom bean mapping and the simplified API
    String tableName = "ACCOUNT";
    int updateCount = Jaqlib.DB.insert(account)
        .into(getJdbcDataSource(), tableName).using(beanMapping);
  }


  public void updateDefaultMapping()
  {
    // get account that already exists at database
    Account account = getAccount();

    String whereClause = "id = " + account.getId();
    String tableName = "ACCOUNT";
    DataSource ds = getJdbcDataSource();
    DbUpdateDataSource dataSource = new DbUpdateDataSource(ds, tableName);
    int updateCount = Jaqlib.DB.update(account).in(dataSource)
        .where(whereClause).usingDefaultMapping();
  }


  public void updateCustomMapping()
  {
    // get account that already exists at database
    Account account = getAccount();

    // create custom bean mapping
    BeanMapping<Account> beanMapping = new BeanMapping<Account>(Account.class);
    beanMapping.removeField("id");
    beanMapping.getField("lastName").setSourceName("lName");

    String whereClause = "id = " + account.getId();
    String tableName = "ACCOUNT";
    int updateCount = Jaqlib.DB.update(account)
        .in(getJdbcDataSource(), tableName).where(whereClause)
        .using(beanMapping);
  }


  public void deleteSpecific()
  {
    // get account that already exists at database
    Account account = getAccount();

    // delete account at database
    String whereClause = "id = " + account.getId();
    String tableName = "ACCOUNT";
    DataSource ds = getJdbcDataSource();
    DbDeleteDataSource dataSource = new DbDeleteDataSource(ds, tableName,
        whereClause);
    int updateCount = Jaqlib.DB.delete().from(dataSource);
  }


  public void deleteAll()
  {
    // delete all records from the ACCOUNT table
    String tableName = "ACCOUNT";
    int updateCount = Jaqlib.DB.delete().from(getJdbcDataSource(), tableName);
  }


  private AccountImpl getAccount()
  {
    // TODO Auto-generated method stub
    return null;
  }
}
