package org.jaqlib;

import static org.jaqlib.util.CollectionUtil.newList;

public class TemperatureSetup
{

  public static final Temperature KITCHEN;
  public static final Temperature CELLAR;

  static
  {
    KITCHEN = new Temperature();
    KITCHEN.setLocation("kitchen");
    KITCHEN.setHistory(newList(25, 26, 23));
    KITCHEN.setSensor(createSensor("TempSens 1.04", "1234",
        createSensorType(1, "temp")));

    CELLAR = new Temperature();
    CELLAR.setLocation("cellar");
    CELLAR.setHistory(newList(19, 18));
    CELLAR.setSensor(createSensor("TempSens 1.02", "1235",
        createSensorType(1, "temp")));
  }


  private static Sensor createSensor(String name, String uniqueId,
      SensorType type)
  {
    Sensor s = new Sensor();
    s.setName(name);
    s.setType(type);
    s.setUniqueId(uniqueId);
    return s;
  }


  private static SensorType createSensorType(int id, String name)
  {
    SensorType type = new SensorType();
    type.setId(id);
    type.setName(name);
    return type;
  }

}
