package org.jaqlib;

import org.jaqlib.core.DefaultsDelegate;


/**
 * Static helper class that holds default infrastructure component instances and
 * global properties.<br>
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


  static
  {
    INSTANCE.reset();
  }


  @Override
  public void reset()
  {
    super.reset();
  }

}
