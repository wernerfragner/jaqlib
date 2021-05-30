package org.jaqlib.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Abstraction for a resource (file, directory, ...).
 * 
 * @author Werner Fragner
 */
public interface Resource
{

  /**
   * Gets an input stream for the resource.
   * 
   * @return see description.
   * @throws IOException in case an error while creating an input stream
   *           occurred.
   */
  InputStream getInputStream() throws IOException;


  /**
   * Gets a URL representation of the resource.
   * 
   * @return see description.
   * @throws MalformedURLException in case the URL is not valid.
   */
  URL getURL() throws MalformedURLException;


  /**
   * See return tag.
   * 
   * @return true if this resource exists physically.
   */
  boolean exists();

}
