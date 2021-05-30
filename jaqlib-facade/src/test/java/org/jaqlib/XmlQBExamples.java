package org.jaqlib;

import org.jaqlib.core.Task;
import org.jaqlib.core.WhereCondition;
import org.jaqlib.core.bean.BeanMapping;
import org.jaqlib.util.ClassPathResource;
import org.jaqlib.util.FileResource;
import org.jaqlib.xml.CreditRatingStringTypeHandler;
import org.jaqlib.xml.XmlSelectDataSource;
import org.jaqlib.xml.xpath.JaxenXPathEngine;
import org.jaqlib.xml.xpath.XalanXPathEngine;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class XmlQBExamples
{

  private static void selectUsingResourcePattern()
  {
    ClassPathResource cpResource = new ClassPathResource(
            "files/Accounts_Attributes.xml");
    FileResource fResource = new FileResource("files/Accounts_Attributes.xml");

    // cpResource and fResoure do not point to the same XML file
    // - cpResource points to a XML file within the application classpath
    // - fResource points to a XML file in the application working directory

    List<AccountImpl> cpAccounts = Jaqlib.XML.select(AccountImpl.class)
        .fromAttributes(cpResource).where("/bank/accounts/*").asList();
    List<AccountImpl> fAccounts = Jaqlib.XML.select(AccountImpl.class)
        .fromAttributes(fResource).where("/bank/accounts/*").asList();
  }


  private static void selectDefault()
  {
    List<? extends Account> accounts = Jaqlib.XML.select(AccountImpl.class)
        .from("Accounts_Attributes.xml").where("/bank/accounts/*").asList();

    List<? extends Account> accounts2 = Jaqlib.XML.select(AccountImpl.class)
        .fromElements("Accounts_Attributes.xml").where("/bank/accounts/*")
        .asList();
  }


  private static void selectElements()
  {
    List<? extends Account> accounts = Jaqlib.XML.select(AccountImpl.class)
        .fromAttributes("Accounts_Elements.xml").where("/bank/accounts/*")
        .asList();
  }


  private static void selectWithMethodRecorder()
  {
    // get recorder object
    Account account = Jaqlib.XML.getRecorder(Account.class);

    // execute query
    List<? extends Account> accountsGreater500 = Jaqlib.XML
        .select(AccountImpl.class).from("Accounts_Elements.xml")
        .where("/bank/accounts/*").andCall(account.getBalance())
        .isGreaterThan(500.0).asList();
  }


  private static void selectWithCustomWhereCondition()
  {
    // create custom WHERE condition
    WhereCondition<AccountImpl> myCondition = new WhereCondition<AccountImpl>()
    {

      @Override
      public boolean evaluate(AccountImpl element)
      {
        if (element == null)
          return false;
        return element.getBalance() > 500;
      }
    };

    // execute query
    List<? extends Account> accountsGreater500 = Jaqlib.XML
        .select(AccountImpl.class).from("Accounts_Elements.xml")
        .where("/bank/accounts/*").and(myCondition).asList();
  }


  private static void selectWithSimpleComparison()
  {
    AccountImpl criteria = new AccountImpl();
    criteria.setId((long) 15);

    Account account15 = Jaqlib.XML.select(AccountImpl.class)
        .from("Accounts_Elements.xml").where("/bank/accounts/*").andElement()
        .isEqual(criteria).uniqueResult();
  }


  public void selectComparable()
  {
    // Account implements the Comparable interface; the balance field is used
    // for comparing two accounts
    AccountImpl criteria = new AccountImpl();
    criteria.setBalance(5000.0);

    List<AccountImpl> result = Jaqlib.XML.select(AccountImpl.class)
        .from("Accounts.xml").where("/bank/accounts/*").andElement()
        .isSmallerThan(criteria).asList();
  }


  private static void selectElementsUsingDataSource()
  {
    // create data source for caching XML file
    XmlSelectDataSource ds = new XmlSelectDataSource("Accounts.xml");
    ds.setAutoClose(false);

    try
    {
      // create recorder for contraining the query
      Account account = Jaqlib.XML.getRecorder(Account.class);

      // execute first query against XML file
      List<? extends Account> allAccounts = Jaqlib.XML
          .select(AccountImpl.class).fromElements(ds).where("/bank/accounts/*")
          .asList();

      // execute second query against XML file
      List<? extends Account> accountsGreater500 = Jaqlib.XML
          .select(AccountImpl.class).from(ds).where("/bank/accounts/*")
          .andCall(account.getBalance()).isGreaterThan(500.0).asList();
    }
    finally
    {
      // data source must be closed manually
      ds.close();
    }
  }


  private static void selectElementsUsingSpecificXPathEngine()
  {
    Jaqlib.XML.DEFAULTS.setXPathEngine(new JaxenXPathEngine());

    // create data source for setting a specific XPath engine
    XmlSelectDataSource ds = new XmlSelectDataSource("Accounts.xml");
    ds.setXPathEngine(new XalanXPathEngine());

    // execute query
    List<? extends Account> allAccounts = Jaqlib.XML.select(AccountImpl.class)
        .fromElements(ds).where("/bank/accounts/*").asList();
  }


  private static void selectUsingCustomBeanMapping()
  {
    // rename field 'lastName' and remove field 'department'
    BeanMapping<Account> mapping = new BeanMapping<Account>(AccountImpl.class);
    mapping.getField("lastName").setSourceName("last_name");
    mapping.removeField("department");

    List<Account> accounts = Jaqlib.XML.select(mapping).from("Accounts.xml")
        .where("/bank/accounts/*").asList();
  }


  private static void selectUsingCustomTypeHandler()
  {
    // alternatively the type handler can be set application-wide
    Jaqlib.XML.DEFAULTS
        .registerJavaTypeHandler(new CreditRatingStringTypeHandler());

    // set a custom type handler for the 'creditRating' field
    BeanMapping<Account> mapping = new BeanMapping<Account>(AccountImpl.class);
    mapping.getField("creditRating").setTypeHandler(
        new CreditRatingStringTypeHandler());

    // execute query
    List<Account> accounts = XmlQB.select(mapping).from("Accounts.xml")
        .where("/bank/accounts/*").asList();
  }


  private static void selectUsingNamespaces()
  {
    Jaqlib.XML.DEFAULTS.addNamespace("bankapp",
        "http://werner.fragner.org/jaqlib/bankapp");

    // register a XML namespace
    XmlSelectDataSource ds = new XmlSelectDataSource("Accounts.xml");
    ds.addNamespace("bankapp", "http://werner.fragner.org/jaqlib/bankapp");

    List<? extends Account> accounts = Jaqlib.XML.select(AccountImpl.class)
        .from(ds).where("/bank/accounts/*").asList();
  }


  private static void selectComplexBean()
  {
    List<? extends Account> accounts = Jaqlib.XML.select(AccountImpl.class)
        .from("Accounts.xml").where("/bank/accounts/*").asList();
  }


  private static void selectComplexBean_OwnMappingLogic()
  {
    BeanMapping<Account> mapping = new BeanMapping<Account>(Account.class);

    // set a custom source name for the transaction collection
    mapping.getCollectionField("transactions").setSourceName(
        "differentTransactions");

    // set a custom source name for the elements of the transaction collection
    mapping.getCollectionField("transactions").setElementSourceName(
        "differentTransaction");

    // execute query
    List<? extends Account> accounts = Jaqlib.XML.select(AccountImpl.class)
        .from("Accounts.xml").where("/bank/accounts/*").asList();
  }


  private static void selectPrimitive()
  {
    List<String> lastNames = Jaqlib.XML.select(String.class)
        .from("Accounts.xml").where("/bank/accounts/account/@lastName")
        .asList();
  }


  private static void selectPrimitive_JavaTypeHandler()
  {
    // register java type handler
    XmlDefaults.INSTANCE
        .registerJavaTypeHandler(new CreditRatingStringTypeHandler());

    // execute query
    List<CreditRating> ratings = Jaqlib.XML.select(CreditRating.class)
        .from("Accounts.xml").where("//@creditRating").asList();
  }


  public void mapResult()
  {
    Account recorder = Jaqlib.XML.getRecorder(Account.class);
    Map<Long, AccountImpl> results = Jaqlib.XML.select(AccountImpl.class)
        .from("Accounts.xml").where("//accounts").asMap(recorder.getId());
  }


  public void setResult()
  {
    Set<AccountImpl> notNullAccounts = Jaqlib.XML.select(AccountImpl.class)
        .from("Accounts.xml").where("//accounts").andElement().isNotNull()
        .asSet();
  }


  public void listResult()
  {
    List<AccountImpl> notNullAccounts = Jaqlib.XML.select(AccountImpl.class)
        .from("Accounts.xml").where("//accounts").andElement().isNotNull()
        .asList();
  }


  public void uniqueResult()
  {
    Account recorder = Jaqlib.XML.getRecorder(Account.class);
    Account result = Jaqlib.XML.select(AccountImpl.class).from("Accounts.xml")
        .where("//accounts").andCall(recorder.getId()).isEqual((long) 5)
        .asUniqueResult();
  }


  public void firstResult()
  {
    Account recorder = Jaqlib.XML.getRecorder(Account.class);
    Account result = Jaqlib.XML.select(AccountImpl.class).from("Accounts.xml")
        .where("//accounts").andCall(recorder.getBalance())
        .isGreaterThan(500.0).asFirstResult();
  }


  public void lastResult()
  {
    Account recorder = Jaqlib.XML.getRecorder(Account.class);
    Account result = Jaqlib.XML.select(AccountImpl.class).from("Accounts.xml")
        .where("//accounts").andCall(recorder.getBalance())
        .isGreaterThan(500.0).asLastResult();
  }


  public void executeTask()
  {
    // create task that should be executed for each element
    Task<Account> task = new Task<Account>()
    {

      public void execute(Account account)
      {
        account.sendInfoEmail();
      }

    };
    Jaqlib.XML.select(AccountImpl.class).from("Accounts.xml")
        .where("//accounts").execute(task);
  }


  public void executeTaskWithResult()
  {
    Task<Account> task = null;

    // create condition for negative balances
    WhereCondition<Account> deptCond = new WhereCondition<Account>()
    {

      public boolean evaluate(Account account)
      {
        return (account.getBalance() < 0);
      }

    };

    // execute task only on elements that match the given condition
    Jaqlib.XML.select(AccountImpl.class).from("Accounts.xml")
        .where("//accounts").and(deptCond).execute(task);

    // or ...
    List<AccountImpl> result = Jaqlib.XML.select(AccountImpl.class)
        .from("Accounts.xml").where("//accounts").and(deptCond)
        .executeWithResult(task).asList();
  }


  public void customBeanFactory()
  {
    // set bean factory application-wide
    CustomBeanFactory beanFactory = new CustomBeanFactory(new EMailComponent());
    Jaqlib.XML.DEFAULTS.setBeanFactory(beanFactory);

    // set bean factory just for one specific mapping
    BeanMapping<Account> mapping = new BeanMapping<Account>(AccountImpl.class);
    mapping.setFactory(beanFactory);

    // perform select
  }

}
