package org.jaqlib.xml;

import static org.jaqlib.AccountAssert.assertHuberAccount;
import static org.jaqlib.AccountAssert.assertMaierAccount;

import java.io.IOException;
import java.util.List;

import junit.framework.TestCase;

import org.jaqlib.Account;
import org.jaqlib.AccountImpl;
import org.jaqlib.TransactionImpl;
import org.jaqlib.XmlDefaults;
import org.jaqlib.XmlQB;
import org.jaqlib.core.Defaults;
import org.jaqlib.util.ClassPathResource;
import org.jaqlib.util.FileResource;

public abstract class XmlQBTest extends TestCase
{

  private static final String XPATH_ACCOUNTS = "/bank/accounts/*";
  private static final String XPATH_ACCOUNTS_NS = "/document/test1:bank/test2:accounts/*";
  private static final String XPATH_TRANSACTIONS = "/bank//transactions/*";


  private List<AccountImpl> accounts;


  @Override
  public void setUp()
  {
    Defaults.getJavaTypeHandlerRegistry().registerTypeHandler(
        new CreditRatingStringTypeHandler());
  }


  @Override
  public void tearDown()
  {
    XmlDefaults.reset();
  }


  public void testSelectAccount_Default_String()
  {
    // select huber account

    accounts = selectDefault(XPATH_ACCOUNTS, "huber");
    assertHuberAccount(accounts);

    // select maier account

    accounts = selectDefault(XPATH_ACCOUNTS, "maier");
    assertMaierAccount(accounts);
  }


  public void testSelectAccount_Attributes_String()
  {
    // select huber account

    accounts = selectAttributes(XPATH_ACCOUNTS, "huber");
    assertHuberAccount(accounts);

    // select maier account

    accounts = selectAttributes(XPATH_ACCOUNTS, "maier");
    assertMaierAccount(accounts);
  }


  public void testSelectAccount_Elements_String()
  {
    // select huber account

    accounts = selectElements(XPATH_ACCOUNTS, "huber");
    assertHuberAccount(accounts);

    // select maier account

    accounts = selectElements(XPATH_ACCOUNTS, "maier");
    assertMaierAccount(accounts);
  }


  public void testSelectAccount_Default_ClassPathResource()
  {
    ClassPathResource resource = new ClassPathResource(
        "accounts_attributes.xml");
    Account recorder = XmlQB.getRecorder(Account.class);
    accounts = XmlQB.select(AccountImpl.class).from(resource).where(
        XPATH_ACCOUNTS).andCall(recorder.getLastName()).isEqual("huber")
        .asList();

    assertHuberAccount(accounts);
  }


  public void testSelectAccount_Attributes_ClassPathResource()
  {
    ClassPathResource resource = new ClassPathResource(
        "accounts_attributes.xml");
    Account recorder = XmlQB.getRecorder(Account.class);
    accounts = XmlQB.select(AccountImpl.class).fromAttributes(resource).where(
        XPATH_ACCOUNTS).andCall(recorder.getLastName()).isEqual("huber")
        .asList();

    assertHuberAccount(accounts);
  }


  public void testSelectAccount_Elements_ClassPathResource()
  {
    ClassPathResource resource = new ClassPathResource("accounts_elements.xml");
    Account recorder = XmlQB.getRecorder(Account.class);
    accounts = XmlQB.select(AccountImpl.class).fromElements(resource).where(
        XPATH_ACCOUNTS).andCall(recorder.getLastName()).isEqual("huber")
        .asList();

    assertHuberAccount(accounts);
  }


  public void testSelectAccount_Default_XmlSelectDataSource()
  {
    FileResource r = new FileResource("unittest/accounts_attributes.xml");
    XmlSelectDataSource ds = new XmlSelectDataSource(r, true);

    Account recorder = XmlQB.getRecorder(Account.class);
    accounts = XmlQB.select(AccountImpl.class).from(ds).where(XPATH_ACCOUNTS)
        .andCall(recorder.getLastName()).isEqual("huber").asList();

    assertHuberAccount(accounts);
  }


  public void testSelectAccount_Attributes_XmlSelectDataSource()
  {
    FileResource r = new FileResource("unittest/accounts_attributes.xml");
    XmlSelectDataSource ds = new XmlSelectDataSource(r, true);

    Account recorder = XmlQB.getRecorder(Account.class);
    accounts = XmlQB.select(AccountImpl.class).fromAttributes(ds).where(
        XPATH_ACCOUNTS).andCall(recorder.getLastName()).isEqual("huber")
        .asList();

    assertHuberAccount(accounts);
  }


  public void testSelectAccount_Elements_XmlSelectDataSource()
  {
    FileResource r = new FileResource("unittest/accounts_elements.xml");
    XmlSelectDataSource ds = new XmlSelectDataSource(r, false);

    Account recorder = XmlQB.getRecorder(Account.class);
    accounts = XmlQB.select(AccountImpl.class).fromElements(ds).where(
        XPATH_ACCOUNTS).andCall(recorder.getLastName()).isEqual("huber")
        .asList();

    assertHuberAccount(accounts);
  }


  public void testSelectAccount_NonExistingFileResource()
  {
    FileResource resource = new FileResource("nonexisting.xml");
    Account recorder = XmlQB.getRecorder(Account.class);

    try
    {
      accounts = XmlQB.select(AccountImpl.class).from(resource).where(
          XPATH_ACCOUNTS).andCall(recorder.getLastName()).isEqual("huber")
          .asList();
      fail("Did not throw RuntimeException");
    }
    catch (RuntimeException e)
    {
      assertEquals(IOException.class, e.getCause().getClass());
    }
  }


  public void testSelectAccount_NonExistingClassPathResource()
  {
    ClassPathResource resource = new ClassPathResource("nonexisting.xml");
    Account recorder = XmlQB.getRecorder(Account.class);

    try
    {
      accounts = XmlQB.select(AccountImpl.class).from(resource).where(
          XPATH_ACCOUNTS).andCall(recorder.getLastName()).isEqual("huber")
          .asList();
      fail("Did not throw RuntimeException");
    }
    catch (RuntimeException e)
    {
      assertEquals(IOException.class, e.getCause().getClass());
    }
  }


  public void testSelectAccount_DataSourceReuse_Default()
  {
    XmlSelectDataSource ds = XmlQB
        .getSelectDataSource("unittest/accounts_attributes.xml");

    Account recorder = XmlQB.getRecorder(Account.class);
    List<AccountImpl> accounts = XmlQB.select(AccountImpl.class).from(ds)
        .where(XPATH_ACCOUNTS).andCall(recorder.getLastName()).isEqual("huber")
        .asList();

    assertHuberAccount(accounts);

    // re-use datasource

    List<TransactionImpl> transactions = XmlQB.select(TransactionImpl.class)
        .from(ds).where(XPATH_TRANSACTIONS).asList();

    assertNotNull(transactions);
    assertEquals(4, transactions.size());
  }


  public void testSelectAccount_Attribute_Namespace()
  {
    addAttributeNamespace("test1", "http://werner.fragner.org/jaqlib/test1");
    addAttributeNamespace("test2", "http://werner.fragner.org/jaqlib/test2");

    // select huber account

    accounts = selectDefault("unittest/accounts_namespaces.xml",
        XPATH_ACCOUNTS_NS, "huber");
    assertHuberAccount(accounts);

    // select maier account

    accounts = selectDefault("unittest/accounts_namespaces.xml",
        XPATH_ACCOUNTS_NS, "maier");
    assertMaierAccount(accounts);
  }


  private List<AccountImpl> selectDefault(String xpathAccounts, String string)
  {
    String fileName = "unittest/accounts_attributes.xml";
    return selectDefault(fileName, xpathAccounts, string);
  }


  private List<AccountImpl> selectDefault(String fileName, String xPath,
      String lastName)
  {
    Account recorder = XmlQB.getRecorder(Account.class);
    return XmlQB.select(AccountImpl.class).from(fileName).where(xPath).andCall(
        recorder.getLastName()).isEqual(lastName).asList();
  }


  private List<AccountImpl> selectAttributes(String xPath, String lastName)
  {
    String fileName = "unittest/accounts_attributes.xml";
    Account recorder = XmlQB.getRecorder(Account.class);
    return XmlQB.select(AccountImpl.class).fromAttributes(fileName)
        .where(xPath).andCall(recorder.getLastName()).isEqual(lastName)
        .asList();
  }


  private List<AccountImpl> selectElements(String xPath, String lastName)
  {
    String fileName = "unittest/accounts_elements.xml";
    Account recorder = XmlQB.getRecorder(Account.class);
    return XmlQB.select(AccountImpl.class).fromElements(fileName).where(xPath)
        .andCall(recorder.getLastName()).isEqual(lastName).asList();
  }


  private void addAttributeNamespace(String prefix, String namespace)
  {
    XmlDefaults.addNamespace(prefix, namespace);
  }

  //
  // public void testXPath() throws Exception
  // {
  // XPathFactory factory = XPathFactory.newInstance();
  //
  // XPath xpath = factory.newXPath();
  // String expression = XPATH_ACCOUNTS;
  // InputSource inputSource = new InputSource(
  // "unittest/accounts_attributes.xml");
  // NodeList nodes = (NodeList) xpath.evaluate(expression, inputSource,
  // XPathConstants.NODESET);
  //
  // List<AccountImpl> accounts = CollectionUtil.newDefaultList();
  // for (int i = 0; i < nodes.getLength(); i++)
  // {
  // Node n = nodes.item(i);
  //
  // AccountImpl account = new AccountImpl();
  //
  // NamedNodeMap attributes = n.getAttributes();
  // account.setLastName(attributes.getNamedItem("lastName").getTextContent());
  //
  // System.out.println(account);
  //
  // accounts.add(account);
  // }
  // }
}
