package org.jaqlib;

import org.jaqlib.core.DefaultsDelegate;


/**
 * Static helper class that holds default infrastructure component instances and
 * application-wide properties.<br>
 * <b>NOTE: Changes to these components/properties have an effect on the entire
 * application. Use with care!</b>
 * 
 * @author Werner Fragner
 */
public class IterableDefaults extends DefaultsDelegate
{

  /**
   * This class must only be instantiated as singleton.
   */
  private IterableDefaults()
  {
  }

  /**
   * Singleton instance of this class.
   */
  public static final IterableDefaults INSTANCE = new IterableDefaults();

  /**
   * Resets all defaults to their initial values.
   */
  static
  {
    INSTANCE.reset();
  }


  /**
   * Resets all defaults to their initial values.
   */
  @Override
  public void reset()
  {
    super.reset();
  }

}
