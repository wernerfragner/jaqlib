package org.jaqlib;

import java.util.List;

import org.jaqlib.query.WhereCondition;

public abstract class AbstractCustomConditonTest<ResultElementType extends SimpleTestElement>
    extends AbstractJaqLibTest<ResultElementType>
{


  public void testSelect_CustomAndCondition()
  {
    List<ResultElementType> elements = createIsMatchElements();

    WhereCondition<ResultElementType> condition = new WhereCondition<ResultElementType>()
    {

      public boolean evaluate(SimpleTestElement element)
      {
        if (element == null)
        {
          return false;
        }
        return element.isMatch();
      }

    };

    List<ResultElementType> results = QB.select(getResultElementClass()).from(
        elements).where(condition).toList();
    assertNotNull(results);
    assertEquals(1, results.size());
    assertSame(elements.get(1), results.get(0));
  }


}
