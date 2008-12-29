package org.jaqlib;

import java.util.List;

import org.jaqlib.query.WhereCondition;

public abstract class AbstractCustomConditonTest<AccountType extends Account>
    extends AbstractJaqLibTest<AccountType>
{


  public void testSelect_CustomAndCondition()
  {
    List<AccountType> elements = createTestAccounts();

    WhereCondition<AccountType> condition = new WhereCondition<AccountType>()
    {

      public boolean evaluate(Account element)
      {
        if (element == null || element.getLastName() == null)
        {
          return false;
        }
        return element.getLastName().equals("maier");
      }

    };

    List<AccountType> results = QB.select(getAccountClass()).from(elements)
        .where(condition).asList();
    assertNotNull(results);
    assertEquals(1, results.size());
    assertSame(elements.get(1), results.get(0));
  }


}
