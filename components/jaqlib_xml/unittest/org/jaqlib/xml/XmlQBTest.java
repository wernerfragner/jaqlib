package org.jaqlib.xml;

import static org.jaqlib.AccountAssert.assertHuberAccount;
import static org.jaqlib.AccountAssert.assertMaierAccount;

import java.util.List;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import junit.framework.TestCase;

import org.jaqlib.Account;
import org.jaqlib.AccountImpl;
import org.jaqlib.TransactionImpl;
import org.jaqlib.XmlQB;
import org.jaqlib.core.Defaults;
import org.jaqlib.util.CollectionUtil;
import org.jaqlib.xml.xpath.XalanXPathEngine;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class XmlQBTest extends TestCase
{


  @Override
  public void setUp()
  {
    Defaults.getJavaTypeHandlerRegistry().registerTypeHandler(
        new CreditRatingStringTypeHandler());
  }


  public void testSelectAccount_SimpleAPI_Attributes()
  {
    // select huber account

    Account recorder = XmlQB.getRecorder(Account.class);
    List<AccountImpl> accounts = XmlQB.select(AccountImpl.class)
        .fromAttributes("unittest/accounts_attributes.xml").where(
            "/bank/accounts/*").andCall(recorder.getLastName())
        .isEqual("huber").asList();

    assertNotNull(accounts);
    assertEquals(1, accounts.size());
    assertHuberAccount(accounts.get(0));

    // select maier account

    accounts = XmlQB.select(AccountImpl.class).fromAttributes(
        "unittest/accounts_attributes.xml").where("/bank/accounts/*").andCall(
        recorder.getLastName()).isEqual("maier").asList();

    assertNotNull(accounts);
    assertEquals(1, accounts.size());
    assertMaierAccount(accounts.get(0));
  }


  public void testSelectAccount_SimpleAPI_Default()
  {
    Account recorder = XmlQB.getRecorder(Account.class);
    List<AccountImpl> accounts = XmlQB.select(AccountImpl.class).from(
        "unittest/accounts_attributes.xml").where("/bank/accounts/*").andCall(
        recorder.getLastName()).isEqual("huber").asList();

    assertNotNull(accounts);
    assertEquals(1, accounts.size());
    assertHuberAccount(accounts.get(0));
  }


  public void testSelectAccount_DataSourceReuse_Default()
  {
    XmlSelectDataSource ds = XmlQB
        .getSelectDataSource("unittest/accounts_attributes.xml");

    Account recorder = XmlQB.getRecorder(Account.class);
    List<AccountImpl> accounts = XmlQB.select(AccountImpl.class).from(ds)
        .where("/bank/accounts/*").andCall(recorder.getLastName()).isEqual(
            "huber").asList();

    assertNotNull(accounts);
    assertEquals(1, accounts.size());
    assertHuberAccount(accounts.get(0));

    // re-use datasource

    List<TransactionImpl> transactions = XmlQB.select(TransactionImpl.class)
        .from(ds).where("/bank//transactions/*").asList();

    assertNotNull(transactions);
    assertEquals(4, transactions.size());
  }


  public void testSelectAccount_SimpleAPI_Elements()
  {
    Account recorder = XmlQB.getRecorder(Account.class);
    AccountImpl account = XmlQB.select(AccountImpl.class).fromElements(
        "unittest/accounts_elements.xml").where("/bank/accounts/*").andCall(
        recorder.getLastName()).isEqual("maier").uniqueResult();

    assertNotNull(account);
    assertMaierAccount(account);
  }


  public void testSelectAccount_CustomXPathEngine()
  {
    XmlSelectDataSource ds = XmlQB
        .getSelectDataSource("unittest/accounts_attributes.xml");
    ds.setXPathEngine(new XalanXPathEngine());

    Account recorder = XmlQB.getRecorder(Account.class);
    List<AccountImpl> accounts = XmlQB.select(AccountImpl.class).from(ds)
        .where("/bank/accounts/*").andCall(recorder.getLastName()).isEqual(
            "huber").asList();

    assertNotNull(accounts);
    assertEquals(1, accounts.size());
    assertHuberAccount(accounts.get(0));
  }


  public void testXPath() throws Exception
  {
    XPathFactory factory = XPathFactory.newInstance();

    XPath xpath = factory.newXPath();
    String expression = "/bank/accounts/*";
    InputSource inputSource = new InputSource(
        "unittest/accounts_attributes.xml");
    NodeList nodes = (NodeList) xpath.evaluate(expression, inputSource,
        XPathConstants.NODESET);

    List<AccountImpl> accounts = CollectionUtil.newDefaultList();
    for (int i = 0; i < nodes.getLength(); i++)
    {
      Node n = nodes.item(i);

      AccountImpl account = new AccountImpl();

      NamedNodeMap attributes = n.getAttributes();
      account.setLastName(attributes.getNamedItem("lastName").getTextContent());

      System.out.println(account);

      accounts.add(account);
    }
  }
}
