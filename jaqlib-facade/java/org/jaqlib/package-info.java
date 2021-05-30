/**
 * This package contains the main entry point of JaQLib: the class 
 * {@link org.jaqlib.Jaqlib}. This class contains references to all available query 
 * builders (short QB) of JaQLib. Currently JaQLib supports following data 
 * sources:<br>
 * <ul>
 * <li>all collections that implement the {@link java.lang.Iterable} interface: {@link org.jaqlib.IterableQB}, {@link org.jaqlib.IterableQueryBuilder}</li>
 * <li>all databases that support JDBC: {@link org.jaqlib.DatabaseQB}, {@link org.jaqlib.DatabaseQueryBuilder}</li>
 * <li>XML files</li>
 * </ul>
 * For each query builder there exists a helper class with static helper 
 * methods. The helper class ends with <tt>QB</tt>, the 
 * actual query builder class ends with <tt>QueryBuilder</tt>.
 * Each query builder also contains a static field DEFAULTS where the 
 * domain specific default values can be retrieved and changed.
 * 
 * @see org.jaqlib.Jaqlib
 * @see org.jaqlib.IterableQB
 * @see org.jaqlib.IterableQueryBuilder
 * @see org.jaqlib.DatabaseQB
 * @see org.jaqlib.DatabaseQueryBuilder
 * @see org.jaqlib.XmlQB
 * @see org.jaqlib.XmlQueryBuilder 
 */
package org.jaqlib;

