package org.jaqlib.util;

import java.io.InputStream;
import java.net.URL;

/**
 * This class represents a resource in the class path of the application.
 * <b>NOTE:</b> by using this class the resource must be specified relative to
 * the class path root(s). Absolute paths are not allowed.
 * 
 * @author Werner Fragner
 */
public class ClassPathResource implements Resource
{

  private ClassLoader classLoader = Thread.currentThread()
      .getContextClassLoader();

  private final String path;


  /**
   * Constructs a new {@link ClassPathResource} object.
   * 
   * @param path the path to the resource (within the application class path).
   */
  public ClassPathResource(String path)
  {
    this.path = Assert.notNull(path);
  }


  /**
   * Sets the class loader that should be used for accessing the resource.
   * 
   * @param classLoader a not null classloader.
   */
  public void setClassLoader(ClassLoader classLoader)
  {
    this.classLoader = Assert.notNull(classLoader);
  }


  /**
   * {@inheritDoc}
   */
  public InputStream getInputStream()
  {
    return classLoader.getResourceAsStream(path);
  }


  /**
   * {@inheritDoc}
   */
  public URL getURL()
  {
    return classLoader.getResource(path);
  }


  /**
   * Returns the path to the resource (within the application class path).
   * 
   * @return see description.
   */
  public String getPath()
  {
    return path;
  }

}
