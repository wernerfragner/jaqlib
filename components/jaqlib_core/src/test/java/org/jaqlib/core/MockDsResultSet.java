package org.jaqlib.core;

import org.jaqlib.AccountImpl;
import org.jaqlib.AccountSetup;
import org.jaqlib.core.bean.FieldMapping;

public class MockDsResultSet implements DsResultSet
{

  private final AccountImpl huber = AccountSetup.HUBER_ACCOUNT;
  private final AccountImpl maier = AccountSetup.MAIER_ACCOUNT;

  private int cnt = 0;


  public Object getObject(FieldMapping<?> mapping)
  {
    Object result = null;
    if (cnt < 2)
    {
      result = getObjectFor(mapping, huber);
    }
    else if (cnt < 4)
    {
      result = getObjectFor(mapping, maier);
    }
    return result;
  }


  private Object getObjectFor(FieldMapping<?> mapping, AccountImpl account)
  {
    if (mapping.getSourceName().equals("id"))
    {
      cnt++;
      return account.getId();
    }
    if (mapping.getSourceName().equals("lastName"))
    {
      cnt++;
      return account.getLastName();
    }
    return DsResultSet.NO_RESULT;
  }


  public boolean next()
  {
    return cnt < 4;
  }


  public Object getAnynomousObject(FieldMapping<?> mapping)
  {
    return getObject(mapping);
  }

}
