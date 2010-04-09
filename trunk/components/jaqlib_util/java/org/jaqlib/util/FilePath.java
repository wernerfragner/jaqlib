package org.jaqlib.util;

import java.io.InputStream;
import java.net.URL;

/**
 * Abstraction for an arbitrary path to a file.
 * 
 * @author Werner Fragner
 */
public class FilePath
{

  private ClassLoader classLoader = Thread.currentThread()
      .getContextClassLoader();

  private final String path;


  public FilePath(String path)
  {
    this.path = path;
  }


  protected void setClassLoader(ClassLoader classLoader)
  {
    this.classLoader = classLoader;
  }


  public InputStream getStream()
  {
    return classLoader.getResourceAsStream(path);
  }


  public URL getURL()
  {
    return classLoader.getResource(path);
  }


  public String getPath()
  {
    return path;
  }

}
