package org.jaqlib;

import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.jaqlib.core.WhereCondition;
import org.jaqlib.core.bean.AbstractJavaTypeHandler;
import org.jaqlib.core.bean.BeanMapping;
import org.jaqlib.db.DbSelectDataSource;

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

      public boolean evaluate(AccountImpl element)
      {
        if (element == null)
          return false;
        return element.getBalance() > 500;
      }
    };

    // execute query
    List<? extends Account> accountsGreater500 = Jaqlib.DB
        .select(AccountImpl.class).from(accounts).where(myCondition).asList();
  }


  public void simpleComparisonMethod()
  {
    long accountId = 15;
    AccountImpl criteria = new AccountImpl();
    criteria.setId(accountId);

    Account account15 = Jaqlib.DB.select(AccountImpl.class).from(accounts)
        .whereElement().isEqual(criteria).uniqueResult();
  }


  public void compareable()
  {
    // Account implements the Comparable interface; the balance field is used
    // for comparing two accounts
    AccountImpl spec = new AccountImpl();
    spec.setBalance(5000.0);

    List<AccountImpl> result = Jaqlib.DB.select(AccountImpl.class)
        .from(accounts).where().element().isSmallerThan(spec).asList();
  }


  public void asMap()
  {
    Account account = Jaqlib.DB.getRecorder(Account.class);
    Map<Long, AccountImpl> results = Jaqlib.DB.select(AccountImpl.class)
        .from(accounts).asMap(account.getId());
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
      if (value instanceof Integer)
        return CreditRating.rating((Integer) value);
      else
        throw handleIllegalInputValue(value, CreditRating.class);
    }


    @Override
    protected void addSupportedTypes(List<Class<?>> types)
    {
      types.add(CreditRating.class);
    }
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


  private AccountImpl getAccount()
  {
    // TODO Auto-generated method stub
    return null;
  }
}
