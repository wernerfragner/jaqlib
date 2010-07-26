package org.jaqlib.util;

import java.net.MalformedURLException;

import junit.framework.TestCase;

public class ClassPathResourceTest extends TestCase
{

  private ClassPathResource resource;


  @Override
  protected void setUp() throws Exception
  {
    super.setUp();

    resource = new ClassPathResource("somefile.xml");
  }


  public void testGetURL() throws MalformedURLException
  {
    String url = resource.getURL().toString();
    assertTrue(url.endsWith("somefile.xml"));
  }


  public void testExists_True()
  {
    assertTrue(resource.exists());
  }


  public void testExists_False()
  {
    resource = new ClassPathResource("nonexisting.txt");
    assertFalse(resource.exists());
  }

}
