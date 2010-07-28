package org.jaqlib;

import java.util.ArrayList;
import java.util.List;


public class Temperature
{

  private String location;
  private List<Integer> history = new ArrayList<Integer>();

  private Sensor sensor;


  public String getLocation()
  {
    return location;
  }


  public void setLocation(String location)
  {
    this.location = location;
  }


  public List<Integer> getHistory()
  {
    return history;
  }


  public void setHistory(List<Integer> history)
  {
    this.history = history;
  }


  public Sensor getSensor()
  {
    return sensor;
  }


  public void setSensor(Sensor sensor)
  {
    this.sensor = sensor;
  }

}
