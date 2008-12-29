/**
 * This package contains all available query builder (short QB) classes of 
 * JaQLib. These classes are the main entry point of JaQLib. Currently JaQLib 
 * supports following data sources:<br>
 * <li>all collections that implement the {@link java.lang.Iterable} interface: {@link org.jaqlib.IterableQB}, {@link org.jaqlib.IterableQueryBuilder}</li>
 * <li>all databases that support JDBC: {@link org.jaqlib.DatabaseQB}, {@link org.jaqlib.DatabaseQueryBuilder}</li>
 * 
 * There always exists a helper class with static helper methods besides the
 * actual query builder class. The helper class ends with <tt>QB</tt>, the 
 * actual query builder class ends with <tt>QueryBuilder</tt>.
 * 
 * @see org.jaqlib.IterableQB
 * @see org.jaqlib.IterableQueryBuilder
 * @see org.jaqlib.DatabaseQB
 * @see org.jaqlib.DatabaseQueryBuilder
 */
package org.jaqlib;

