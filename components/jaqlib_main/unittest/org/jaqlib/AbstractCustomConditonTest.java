package org.jaqlib;

import java.util.List;

import org.jaqlib.query.WhereCondition;

public abstract class AbstractCustomConditonTest<ResultItemType extends SimpleTestItem>
    extends AbstractJaqLibTest<ResultItemType>
{


  public void testSelect_CustomAndCondition()
  {
    List<? extends ResultItemType> items = createIsMatchItems();

    WhereCondition<ResultItemType> condition = new WhereCondition<ResultItemType>()
    {

      public boolean evaluate(SimpleTestItem item)
      {
        if (item == null)
        {
          return false;
        }
        return item.isMatch();
      }

    };

    List<ResultItemType> results = QB.select(getResultItemClass()).from(items)
        .where(condition).toList();
    assertNotNull(results);
    assertEquals(1, results.size());
    assertSame(items.get(1), results.get(0));
  }


}
