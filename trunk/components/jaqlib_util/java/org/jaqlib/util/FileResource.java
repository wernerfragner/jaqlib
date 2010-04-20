package org.jaqlib.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * This class represents a resource in the file system. The resource can be
 * specified relative to the execution directory of this application or absolute
 * to the file system root.
 * 
 * @author Werner Fragner
 */
public class FileResource implements Resource
{

  private final File file;


  /**
   * Constructs a new {@link FileResource} using the given path (can be relative
   * or absolute).
   * 
   * @param path a not null path.
   */
  public FileResource(String path)
  {
    this.file = new File(path);
  }


  /**
   * {@inheritDoc}
   */
  public InputStream getInputStream() throws IOException
  {
    return new FileInputStream(file);
  }


  /**
   * {@inheritDoc}
   */
  public URL getURL() throws MalformedURLException
  {
    return file.toURI().toURL();
  }

}
