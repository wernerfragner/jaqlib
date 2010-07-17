package org.jaqlib;

import java.util.List;

import org.jaqlib.core.bean.BeanMapping;
import org.jaqlib.util.ClassPathResource;
import org.jaqlib.util.FileResource;
import org.jaqlib.xml.XmlSelectDataSource;
import org.jaqlib.xml.xpath.JaxenXPathEngine;
import org.jaqlib.xml.xpath.XalanXPathEngine;

public class XmlQBExamples
{

  private static void selectUsingResourcePattern()
  {
    ClassPathResource cpResource = new ClassPathResource(
        "./files/Accounts_Attributes.xml");
    FileResource fResource = new FileResource(
        "./JaqlibExample.jar!/files/Accounts_Attributes.xml");

    // cpResource and fResoure point to the same XML file
    // queries access the same XML file

    List<? extends Account> cpAccounts = Jaqlib.XML.select(AccountImpl.class)
        .fromAttributes(cpResource).where("/bank/accounts/*").asList();
    List<? extends Account> fAccounts = Jaqlib.XML.select(AccountImpl.class)
        .fromAttributes(fResource).where("/bank/accounts/*").asList();
  }


  private static void selectDefault()
  {
    // using the method call mechanism for constraining the query result
    Account account = Jaqlib.XML.getRecorder(Account.class);
    List<? extends Account> accountsGreater500 = Jaqlib.XML.select(
        AccountImpl.class).from("Accounts_Elements.xml").where(
        "/bank/accounts/*").andCall(account.getBalance()).isGreaterThan(500.0)
        .asList();
  }


  private static void selectAttributes()
  {
    List<? extends Account> accounts = Jaqlib.XML.select(AccountImpl.class)
        .fromAttributes("Accounts_Attributes.xml").where("/bank/accounts/*")
        .asList();
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
      List<? extends Account> accountsGreater500 = Jaqlib.XML.select(
          AccountImpl.class).from(ds).where("/bank/accounts/*").andCall(
          account.getBalance()).isGreaterThan(500.0).asList();
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
    BeanMapping<Account> mapping = new BeanMapping<Account>(Account.class);
    mapping.getChildField("lastName").setSourceName("last_name");
    mapping.removeChildColumn("department");

    List<? extends Account> accounts = Jaqlib.XML.select(mapping).from(
        "Accounts.xml").where("/bank/accounts/*").asList();
  }
}
