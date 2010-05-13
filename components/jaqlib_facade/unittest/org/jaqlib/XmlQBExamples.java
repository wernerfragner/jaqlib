package org.jaqlib;

import java.util.List;

import javax.sql.DataSource;

public class XmlQBExamples
{

  public static void main(String[] args)
  {
    String xmlPath = "";
    String xPath = "";

    List<? extends Account> accounts = Jaqlib.XML.select(AccountImpl.class)
        .from(xmlPath).where(xPath).asList();

    DataSource ds = null;
    String sql = "";
    List<? extends Account> dbAccounts = Jaqlib.DB.select(AccountImpl.class)
        .from(ds, sql).asList();
  }
}
