package org.jaqlib;

import org.jaqlib.core.WhereCondition;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public abstract class AbstractCustomConditonTest<AccountType extends Account>
    extends AbstractJaqLibTest<AccountType>
{

  @Test
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

    List<AccountType> results = IterableQB.select().from(elements).where(
        condition).asList();
    assertNotNull(results);
    assertEquals(1, results.size());
    assertSame(elements.get(1), results.get(0));
  }

}
