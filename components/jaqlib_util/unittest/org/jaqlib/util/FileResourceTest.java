package org.jaqlib.util;

import java.net.MalformedURLException;

import junit.framework.TestCase;

public class FileResourceTest extends TestCase
{

  private FileResource resource;


  @Override
  protected void setUp() throws Exception
  {
    super.setUp();

    resource = new FileResource("unittest/somefile.xml");
  }


  public void testGetURL() throws MalformedURLException
  {
    String url = resource.getURL().toString();
    assertTrue(url.startsWith("file:"));
    assertTrue(url.endsWith("unittest/somefile.xml"));
  }


  public void testExists_True()
  {
    assertTrue(resource.exists());
  }


  public void testExists_False()
  {
    resource = new FileResource("nonexisting.txt");
    assertFalse(resource.exists());
  }

}
