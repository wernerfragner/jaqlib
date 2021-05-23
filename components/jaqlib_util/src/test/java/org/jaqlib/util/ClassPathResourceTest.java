package org.jaqlib.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.MalformedURLException;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class ClassPathResourceTest
{

  private ClassPathResource resource;


  @BeforeEach
  void setUp()
  {
    resource = new ClassPathResource("somefile.xml");
  }


  @Test
  void testGetURL()
  {
    String url = resource.getURL().toString();
    assertTrue(url.endsWith("somefile.xml"));
  }

  @Test
  void testExists_True()
  {
    assertTrue(resource.exists());
  }

  @Test
  void testExists_False()
  {
    resource = new ClassPathResource("nonexisting.txt");
    assertFalse(resource.exists());
  }

}
