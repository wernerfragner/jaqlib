package org.jaqlib;

public class Sensor
{

  private String name;
  private String uniqueId;
  private SensorType type;


  public String getName()
  {
    return name;
  }


  public void setName(String name)
  {
    this.name = name;
  }


  public String getUniqueId()
  {
    return uniqueId;
  }


  public void setUniqueId(String uniqueId)
  {
    this.uniqueId = uniqueId;
  }


  public SensorType getType()
  {
    return type;
  }


  public void setType(SensorType type)
  {
    this.type = type;
  }

}
