package org.jaqlib;

import java.util.List;

import org.jaqlib.query.WhereCondition;

public abstract class AbstractCustomConditonTest<AccountType extends Account>
    extends AbstractJaqLibTest<AccountType>
{


  public void testSelect_CustomAndCondition()
  {
    List<AccountType> elements = createIsMatchElements();

    WhereCondition<AccountType> condition = new WhereCondition<AccountType>()
    {

      public boolean evaluate(Account element)
      {
        if (element == null)
        {
          return false;
        }
        return element.isMatch();
      }

    };

    List<AccountType> results = QB.select(getAccountClass()).from(
        elements).where(condition).toList();
    assertNotNull(results);
    assertEquals(1, results.size());
    assertSame(elements.get(1), results.get(0));
  }


}
