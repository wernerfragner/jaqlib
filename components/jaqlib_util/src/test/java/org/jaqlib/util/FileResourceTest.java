package org.jaqlib.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.net.MalformedURLException;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class FileResourceTest
{

  FileResource resource;


  @BeforeEach
  void setUp()
  {
    resource = new FileResource("src/test/resources/somefile.xml");
  }


  @Test
  void testGetURL() throws MalformedURLException
  {
    String url = resource.getURL().toString();
    assertTrue(url.startsWith("file:"));
    assertTrue(url.endsWith("src/test/resources/somefile.xml"));
  }

  @Test
  void testExists_True()
  {
    assertTrue(resource.exists());
  }

  @Test
  void testExists_False()
  {
    resource = new FileResource("nonexisting.txt");
    assertFalse(resource.exists());
  }

}
