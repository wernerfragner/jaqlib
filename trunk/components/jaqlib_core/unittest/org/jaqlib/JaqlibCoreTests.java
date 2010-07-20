package org.jaqlib;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.jaqlib.core.CachingFetchStrategyTest;
import org.jaqlib.core.QueryCacheTest;
import org.jaqlib.core.bean.BeanMappingTest;
import org.jaqlib.core.bean.DefaultBeanFactoryTest;
import org.jaqlib.core.bean.DefaultJavaTypeHandlerRegistryTest;

public class JaqlibCoreTests
{

  public static Test suite()
  {
    TestSuite suite = new TestSuite("Test for org.jaqlib");
    // $JUnit-BEGIN$
    suite.addTestSuite(DefaultsTest.class);
    suite.addTestSuite(BeanConventionMappingStrategyTest.class);
    suite.addTestSuite(BeanMappingTest.class);
    suite.addTestSuite(DefaultBeanFactoryTest.class);
    suite.addTestSuite(DefaultJavaTypeHandlerRegistryTest.class);
    suite.addTestSuite(CachingFetchStrategyTest.class);
    suite.addTestSuite(QueryCacheTest.class);
    // $JUnit-END$
    return suite;
  }

}
