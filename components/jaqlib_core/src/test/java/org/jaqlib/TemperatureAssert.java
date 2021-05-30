package org.jaqlib;


import static org.junit.jupiter.api.Assertions.*;

public class TemperatureAssert
{

  private static final Temperature KITCHEN = TemperatureSetup.KITCHEN;
  private static final Temperature CELLAR = TemperatureSetup.CELLAR;


  public static void assertKitchenTemperature(Temperature temp)
  {
    assertNotNull(temp);
    assertEquals(KITCHEN.getLocation(), temp.getLocation());
    JaqlibAssert.assertEqualLists(KITCHEN.getHistory(), temp.getHistory());
  }


  public static void assertKitchenSensor(Sensor actual)
  {
    assertNotNull(actual);

    Sensor expected = KITCHEN.getSensor();
    assertEquals(expected.getName(), actual.getName());
    assertEquals(expected.getUniqueId(), actual.getUniqueId());

    SensorType expectedType = expected.getType();
    SensorType actualType = actual.getType();

    assertEquals(expectedType.getId(), actualType.getId());
    assertEquals(expectedType.getName(), actualType.getName());
  }

}
